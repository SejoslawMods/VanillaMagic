package seia.vanillamagic.items.potionedcrystal;

import java.util.List;

import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.spell.EnumWand;
import seia.vanillamagic.utils.CauldronHelper;
import seia.vanillamagic.utils.EntityHelper;

public class QuestPotionedCrystal extends Quest
{
	public QuestPotionedCrystal(Quest required, int posX, int posY, ItemStack icon, String questName,
			String uniqueName) 
	{
		super(required, posX, posY, icon, questName, uniqueName);
	}
	
	int countTicks = 0;
	@SubscribeEvent
	public void craftPotionedCrystal(RightClickBlock event)
	{
		if(countTicks == 0)
		{
			countTicks++;
		}
		else
		{
			countTicks = 0;
			return;
		}
		World world = event.getWorld();
		EntityPlayer player = event.getEntityPlayer();
		BlockPos clickedPos = event.getPos();
		ItemStack stackRightHand = player.getHeldItemMainhand();
		if(stackRightHand == null)
		{
			return;
		}
		if(!player.isSneaking())
		{
			return;
		}
		if(EnumWand.areWandsEqual(stackRightHand, EnumWand.BLAZE_ROD.wandItemStack))
		{
			if(world.getBlockState(clickedPos).getBlock() instanceof BlockCauldron)
			{
				List<EntityItem> itemsInCauldron = CauldronHelper.getItemsInCauldron(world, clickedPos);
				if(itemsInCauldron.size() == 0)
				{
					return;
				}
				else if(itemsInCauldron.size() == 2)
				{
					boolean ns = false;
					for(EntityItem item : itemsInCauldron)
					{
						if(item.getEntityItem().getItem().equals(Items.NETHER_STAR))
						{
							ns = true;
							break;
						}
					}
					IPotionedCrystal ipc = PotionedCrystalHelper.getPotionedCrystalFromCauldron(world, clickedPos);
					if(ns == true && ipc != null)
					{
						if(!player.hasAchievement(achievement))
						{
							player.addStat(achievement, 1);
						}
						if(player.hasAchievement(achievement))
						{
							EntityItem newEI = new EntityItem(world, clickedPos.getX(), clickedPos.getY() + 1, clickedPos.getZ(), ipc.getItem().copy());
							world.spawnEntityInWorld(newEI);
							EntityHelper.removeEntities(world, itemsInCauldron);
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onWearTick(PlayerTickEvent event)
	{
		EntityPlayer player = event.player;
		InventoryPlayer inventory = player.inventory;
		ItemStack[] mainInv = inventory.mainInventory;
		for(ItemStack stack : mainInv)
		{
			if(stack != null)
			{
				IPotionedCrystal ipc = PotionedCrystalHelper.getPotionedCrystal(stack);
				if(ipc != null)
				{
					if(!player.hasAchievement(achievement))
					{
						player.addStat(achievement, 1);
					}
					if(player.hasAchievement(achievement))
					{
						List<PotionEffect> effects = ipc.getPotionType().getEffects();
						for(PotionEffect pe : effects)
						{
							PotionEffect newPE = new PotionEffect(pe.getPotion(), 100, pe.getAmplifier(), pe.getIsAmbient(), pe.doesShowParticles());
							player.addPotionEffect(newPE);
						}
					}
				}
			}
		}
	}
}