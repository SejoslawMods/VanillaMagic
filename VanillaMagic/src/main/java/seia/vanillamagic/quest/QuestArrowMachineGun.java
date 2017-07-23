package seia.vanillamagic.quest;

import java.util.Random;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.magic.wand.WandRegistry;
import seia.vanillamagic.util.ItemStackUtil;

public class QuestArrowMachineGun extends Quest
{
	public boolean checkHands(EntityPlayer player, ItemStack leftHand, ItemStack rightHand)
	{
		if (leftHand.getItem().equals(Items.ARROW) || leftHand.getItem().equals(Items.TIPPED_ARROW))
			if (rightHand.getItem().equals(WandRegistry.WAND_NETHER_STAR.getWandStack().getItem()))
				return true;
		return false;
	}
	
	@SubscribeEvent
	public void onRightClick(RightClickItem event)
	{
		EntityPlayer player = event.getEntityPlayer();
		ItemStack leftHand = player.getHeldItemOffhand();
		ItemStack rightHand = player.getHeldItemMainhand();
		World world = player.world;
		if (ItemStackUtil.isNullStack(leftHand)) return;
		
		if (checkHands(player, leftHand, rightHand))
		{
			checkQuestProgress(player);
			
			if (hasQuest(player))
			{
				EntityTippedArrow entityTippedArrow = new EntityTippedArrow(world, player);
				entityTippedArrow.setPotionEffect(leftHand);
				entityTippedArrow.setAim(player, 
						player.rotationPitch,
						player.rotationYaw,
						0.0F,
						3.0F,
						1.0F);
				entityTippedArrow.setIsCritical(true);
				int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, leftHand);
				if (j > 0) entityTippedArrow.setDamage(entityTippedArrow.getDamage() + (double)j * 0.5D + 0.5D);
				
				int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, leftHand);
				if (k > 0) entityTippedArrow.setKnockbackStrength(k);
				
				if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, leftHand) > 0) entityTippedArrow.setFire(100);
				
				world.spawnEntity(entityTippedArrow);
				world.playSound((EntityPlayer)null, 
						player.posX, 
						player.posY, 
						player.posZ, 
						SoundEvents.ENTITY_ARROW_SHOOT, 
						SoundCategory.NEUTRAL, 
						1.0F, 
						1.0F / (new Random().nextFloat() * 0.4F + 1.2F) + 0.5F);
				if (ItemStackUtil.getStackSize(leftHand) > 0)
				{
					ItemStackUtil.decreaseStackSize(leftHand, 1);
					if (ItemStackUtil.getStackSize(leftHand) <= 0) player.inventory.deleteStack(leftHand);
				}
			}
		}
	}
	
	@SubscribeEvent
	public void showTooltip(ItemTooltipEvent event)
	{
		EntityPlayer player = event.getEntityPlayer();
		if (player == null) return;
		
		ItemStack leftHand = player.getHeldItemOffhand();
		ItemStack rightHand = player.getHeldItemMainhand();
		if (ItemStackUtil.isNullStack(leftHand)) return;
		
		if (checkHands(player, leftHand, rightHand))
		{
			ItemStack eventStack = event.getItemStack();
			if (ItemStack.areItemsEqual(eventStack, WandRegistry.WAND_NETHER_STAR.getWandStack())
					|| eventStack.getItem().equals(Items.ARROW)
					|| eventStack.getItem().equals(Items.TIPPED_ARROW))
			{
				event.getToolTip().add("Hold down Right-Mouse-Button to fire arrows very fast.");
			}
		}
	}
}