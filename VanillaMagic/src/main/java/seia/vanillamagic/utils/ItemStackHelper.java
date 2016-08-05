package seia.vanillamagic.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemStackHelper 
{
	private ItemStackHelper()
	{
	}
	
	public static ItemStack getLapis(int amount)
	{
		return new ItemStack(Items.DYE, amount, 4);
	}
	
	public static ItemStack getSugarCane(int amount)
	{
		return new ItemStack(Items.REEDS, amount);
	}

	/*
	 * 0 - Skeleton
	 * 1 - Wither Skeleton
	 * 2 - Zombie
	 * 3 - Steve
	 * 4 - Creeper
	 * 5 - Ender Dragon
	 */
	public static ItemStack getHead(int amount, int meta)
	{
		return new ItemStack(Items.SKULL, amount, meta);
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
	
	public static ItemStack getItemStackWithNextMetadata(ItemStack stack)
	{
		ItemStack stackWithNextMeta = null;
		Item item = stack.getItem();
		int amount = stack.stackSize;
		int meta = stack.getItemDamage();
		try
		{
			stackWithNextMeta = new ItemStack(item, amount, meta + 1);
		}
		catch(Exception e)
		{
			stackWithNextMeta = new ItemStack(item, amount, 0);
		}
		return stackWithNextMeta;
	}
	
	public static ItemStack replaceItemInStack(ItemStack stack, Item item)
	{
		ItemStack newStack = stack.copy();
		newStack.setItem(item);
		return newStack;
	}
}