package seia.vanillamagic.util;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

/**
 * Class which store various methods connected with Bow.
 */
public class BowUtil 
{
	private BowUtil()
	{
	}
	
	public static boolean isArrow(@Nullable ItemStack stack)
	{
		return !ItemStackUtil.isNullStack(stack) && stack.getItem() instanceof ItemArrow;
	}
	
	@Nullable
	public static ItemStack findAmmo(EntityPlayer player)
	{
		if (isArrow(player.getHeldItem(EnumHand.OFF_HAND))) return player.getHeldItem(EnumHand.OFF_HAND);
		else if (isArrow(player.getHeldItem(EnumHand.MAIN_HAND))) return player.getHeldItem(EnumHand.MAIN_HAND);
		else
		{
			for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
			{
				ItemStack itemstack = player.inventory.getStackInSlot(i);
				if (isArrow(itemstack)) return itemstack;
			}
			return ItemStackUtil.NULL_STACK;
		}
	}
}