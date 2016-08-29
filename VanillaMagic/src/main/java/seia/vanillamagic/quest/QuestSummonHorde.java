package seia.vanillamagic.quest;

import java.util.Random;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.spell.EnumWand;
import seia.vanillamagic.utils.EntityHelper;

public class QuestSummonHorde extends Quest
{
	protected int range = 30; // range in blocks
	protected int verticalRange = 3; // vertical range in blocks
	protected int level; // it will tell how many monsters will be summoned
	protected ItemStack requiredLeftHand;
	
	public QuestSummonHorde(Quest required, int posX, int posY, String questName, String uniqueName,
			int level, ItemStack requiredLeftHand) 
	{
		super(required, posX, posY, requiredLeftHand, questName, uniqueName);
		this.level = level;
		this.requiredLeftHand = requiredLeftHand;
	}
	
	@SubscribeEvent
	public void spawnHorde(RightClickBlock event)
	{
		EntityPlayer player = event.getEntityPlayer();
		World world = event.getWorld();
		ItemStack rightHand = player.getHeldItemMainhand();
		if(rightHand == null)
		{
			return;
		}
		ItemStack leftHand = player.getHeldItemOffhand();
		if(leftHand == null)
		{
			return;
		}
		if(EnumWand.areWandsEqual(EnumWand.getWandByItemStack(rightHand), EnumWand.NETHER_STAR))
		{
			if(ItemStack.areItemsEqual(leftHand, requiredLeftHand))
			{
				if(leftHand.stackSize == requiredLeftHand.stackSize)
				{
					if(!player.hasAchievement(achievement))
					{
						player.addStat(achievement, 1);
					}
					if(player.hasAchievement(achievement))
					{
						EntityHelper.addChatComponentMessage(player, player.getDisplayNameString() + " summoned hord lvl: " + this.level);
						spawnHorde(player, world);
						leftHand.stackSize -= requiredLeftHand.stackSize;
					}
				}
			}
		}
	}

	public void spawnHorde(EntityPlayer player, World world) 
	{
		int posX = (int) Math.round(player.posX - 0.5f);
		int posY = (int) player.posY;
		int posZ = (int) Math.round(player.posZ - 0.5f);
		int spawnX = 0;
		int spawnY = 0;
		int spawnZ = 0;
		for(int i = 0; i < level; i++)
		{
			spawnX = countNewPos(posX, range);
			spawnY = countNewPos(posY, verticalRange);
			spawnZ = countNewPos(posZ, range);
			BlockPos spawnPos = new BlockPos(spawnX, spawnY, spawnZ);
			spawn(player, world, spawnPos);
		}
	}
	
	Random rand = new Random();
	public int countNewPos(int pos, int range) 
	{
		if(rand.nextInt(100) > 50) // spawn on coord positive or negative
		{
			pos += rand.nextInt(range);
		}
		else
		{
			pos -= rand.nextInt(range);
		}
		return pos;
	}
	
	/**
	 * Spawn single enemy.
	 */
	public void spawn(EntityPlayer player, World world, BlockPos spawnPos) 
	{
		EntityLiving entity = EntityHelper.getRandomHostileMob(world);
		entity = addRandomItemToSlot(entity, EntityEquipmentSlot.CHEST);
		entity = addRandomItemToSlot(entity, EntityEquipmentSlot.FEET);
		entity = addRandomItemToSlot(entity, EntityEquipmentSlot.HEAD);
		entity = addRandomItemToSlot(entity, EntityEquipmentSlot.LEGS);
		entity = addRandomItemToSlot(entity, EntityEquipmentSlot.MAINHAND);
		world.spawnEntityInWorld(entity);
	}

	public EntityLiving addRandomItemToSlot(EntityLiving entity, EntityEquipmentSlot slot) 
	{
		entity.setItemStackToSlot(slot, EntityHelper.getRandomItemForSlot(slot));
		return entity;
	}
}