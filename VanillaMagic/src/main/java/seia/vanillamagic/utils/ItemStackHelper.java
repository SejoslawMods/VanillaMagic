package seia.vanillamagic.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ItemStackHelper 
{
	public static ItemStack getLapis()
	{
		return new ItemStack(Items.DYE, 1, 4);
	}
	
	public static ItemStack getSugarCane()
	{
		return new ItemStack(Blocks.REEDS);
	}
	
	public static boolean checkItemsInHands(EntityPlayer player, 
			ItemStack shouldHaveInOffHand, ItemStack shouldHaveInMainHand)
	{
		ItemStack offHand = player.getHeldItemOffhand();
		ItemStack mainHand = player.getHeldItemMainhand();
		if(ItemStack.areItemStacksEqual(offHand, shouldHaveInOffHand))
		{
			if(ItemStack.areItemStacksEqual(mainHand, shouldHaveInMainHand))
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean printItemStackInfo(ItemStack stack, String[] additionalInfo)
	{
		if(stack != null)
		{
			System.out.println("ItemStack Info");
			System.out.println("Item = " + stack.getItem().toString());
			System.out.println("StackSize = " + stack.stackSize);
			System.out.println("ItemDamage = " + stack.getItemDamage());
			for(int i = 0; i < additionalInfo.length; i++)
			{
				System.out.println("Additional Info #" + i + " = " + additionalInfo[i]);
			}
		}
		return false;
	}
}