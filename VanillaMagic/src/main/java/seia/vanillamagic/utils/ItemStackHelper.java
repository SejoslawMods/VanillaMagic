package seia.vanillamagic.utils;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
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
}