package seia.vanillamagic.item.potionedcrystal;

import java.util.List;

import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import seia.vanillamagic.api.event.EventPotionedCrystalTick;
import seia.vanillamagic.magic.wand.WandRegistry;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.util.CauldronUtil;
import seia.vanillamagic.util.EntityUtil;
import seia.vanillamagic.util.ItemStackUtil;

/**
 * Class which describes Quest used by PotionedCrystals.
 */
public class QuestPotionedCrystal extends Quest
{
	int countTicks = 0;
	/**
	 * Craft single PotionedCrystal.
	 */
	@SubscribeEvent
	public void craftPotionedCrystal(RightClickBlock event)
	{
		if (countTicks == 0) countTicks++;
		else
		{
			countTicks = 0;
			return;
		}
		
		World world = event.getWorld();
		EntityPlayer player = event.getEntityPlayer();
		BlockPos clickedPos = event.getPos();
		ItemStack stackRightHand = player.getHeldItemMainhand();
		if (ItemStackUtil.isNullStack(stackRightHand)) return;
		if (!player.isSneaking()) return;
		
		if (WandRegistry.areWandsEqual(stackRightHand, WandRegistry.WAND_BLAZE_ROD.getWandStack()))
		{
			if (world.getBlockState(clickedPos).getBlock() instanceof BlockCauldron)
			{
				List<EntityItem> itemsInCauldron = CauldronUtil.getItemsInCauldron(world, clickedPos);
				if (itemsInCauldron.size() == 0) return;
				else if (itemsInCauldron.size() == 2)
				{
					boolean ns = false;
					for (EntityItem item : itemsInCauldron)
					{
						if (item.getItem().getItem().equals(Items.NETHER_STAR))
						{
							ns = true;
							break;
						}
					}
					IPotionedCrystal ipc = PotionedCrystalHelper.getPotionedCrystalFromCauldron(world, clickedPos);
					if (ns == true && ipc != null)
					{
						if (!hasQuest(player)) addStat(player);
						
						if (hasQuest(player))
						{
							EntityItem newEI = new EntityItem(world, clickedPos.getX(), clickedPos.getY() + 1, clickedPos.getZ(), ipc.getItem().copy());
							world.spawnEntity(newEI);
							EntityUtil.removeEntities(world, itemsInCauldron);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Use Potion effect when Player holds a PotionedCrystal inside inventory.
	 */
	@SubscribeEvent
	public void onWearTick(PlayerTickEvent event)
	{
		EntityPlayer player = event.player;
		InventoryPlayer inventory = player.inventory;
		NonNullList<ItemStack> mainInv = inventory.mainInventory;
		for (ItemStack stack : mainInv)
		{
			if (!ItemStackUtil.isNullStack(stack))
			{
				IPotionedCrystal ipc = PotionedCrystalHelper.getPotionedCrystal(stack);
				if (ipc != null)
				{
					if (!hasQuest(player)) addStat(player);
					
					if (hasQuest(player))
					{
						List<PotionEffect> effects = ipc.getPotionType().getEffects();
						for (PotionEffect pe : effects)
						{
							PotionEffect newPE = new PotionEffect(pe.getPotion(), 100, pe.getAmplifier(), pe.getIsAmbient(), pe.doesShowParticles());
							if (!MinecraftForge.EVENT_BUS.post(new EventPotionedCrystalTick(player, stack, newPE))) player.addPotionEffect(newPE);
						}
					}
				}
			}
		}
	}
}