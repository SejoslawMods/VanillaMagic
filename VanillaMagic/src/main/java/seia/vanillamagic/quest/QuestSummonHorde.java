package seia.vanillamagic.quest;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.spell.EnumSpell;
import seia.vanillamagic.spell.EnumWand;
import seia.vanillamagic.spell.SpellHelper;
import seia.vanillamagic.utils.EntityHelper;
import seia.vanillamagic.utils.ListHelper;

public class QuestSummonHorde extends Quest
{
	protected int level; // it will tell how many monsters will be summoned
	protected int range = 10; // range in blocks
	protected int verticalRange = 1; // vertical range in blocks
	// the number is amount of blocks in which the enemy can spawn, requiredDistanceToPlayer away from Player
	protected double requiredDistanceToPlayer = range - 2; 
	protected final ItemStack requiredLeftHand;
	
	public QuestSummonHorde(Quest required, int posX, int posY, String questName, String uniqueName,
			int level, ItemStack requiredLeftHand, Quest[] requiredQuests) 
	{
		super(required, posX, posY, requiredLeftHand, questName, uniqueName, requiredQuests);
		this.level = level;
		this.requiredLeftHand = requiredLeftHand;
	}
	
	@SubscribeEvent
	public void spawnHorde(RightClickBlock event)
	{
		EntityPlayer player = event.getEntityPlayer();
		World world = player.worldObj;
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
		EnumWand wandRightHand = EnumWand.getWandByItemStack(rightHand);
		if(wandRightHand == null)
		{
			return;
		}
		if(EnumWand.areWandsEqual(wandRightHand, EnumWand.NETHER_STAR))
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
						EntityHelper.addChatComponentMessage(player, player.getDisplayNameString() + " summoned horde lvl: " + this.level + ". Prepare to DIE !!!");
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
		for(int i = 0; i < level; i++)
		{
			for(int ix = posX - range; ix <= posX + range; ix++)
			{
				for(int iz = posZ - range; iz <= posZ + range; iz++)
				{
					for(int iy = posY - verticalRange; iy <= posY + verticalRange; iy++)
					{
						BlockPos spawnPos = new BlockPos(ix, iy, iz);
						double distanceToPlayer = spawnPos.getDistance(posX, posY, posZ);
						if(distanceToPlayer >= requiredDistanceToPlayer)
						{
							if(world.rand.nextInt(50) == 0)
							{
								spawn(player, world, spawnPos);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Spawn single enemy.
	 */
	public void spawn(EntityPlayer player, World world, BlockPos spawnPos) 
	{
		int randID = ListHelper.getRandomObjectFromTab(EnumSpell.getSummonMobSpellIDsWithoutSpecific(EnumSpell.SUMMON_WITHER.spellID));
		SpellHelper.spellSummonMob(player, spawnPos, EnumFacing.UP, null, randID, true);
	}
}