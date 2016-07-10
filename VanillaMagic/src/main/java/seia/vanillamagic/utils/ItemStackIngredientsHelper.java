package seia.vanillamagic.utils;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ItemStackIngredientsHelper 
{
	public static ItemStack getLapis()
	{
		return new ItemStack(Items.DYE, 1, 4);
	}
	
	public static ItemStack getSugarCane()
	{
		return new ItemStack(Blocks.REEDS);
	}
	
	public static ItemStack[] getCraftIronOreIngredients()
	{
		return new ItemStack[]
		{
				new ItemStack(Blocks.SAPLING), new ItemStack(Blocks.SAPLING), new ItemStack(Blocks.SAPLING),
				new ItemStack(Blocks.SAPLING), new ItemStack(Blocks.SAPLING), new ItemStack(Blocks.SAPLING),
				new ItemStack(Blocks.SAPLING), new ItemStack(Blocks.SAPLING), new ItemStack(Blocks.SAPLING),
				new ItemStack(Blocks.SAPLING),
				new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.COBBLESTONE),
				new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.COBBLESTONE),
				new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.COBBLESTONE), new ItemStack(Blocks.COBBLESTONE),
				new ItemStack(Blocks.COBBLESTONE)
		};
	}
	
	public static ItemStack[] getCraftGoldIngotIngredients()
	{
		return new ItemStack[]
		{
				new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT),
				new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT),
				new ItemStack(Items.IRON_INGOT), new ItemStack(Items.IRON_INGOT)
		};
	}
	
	public static ItemStack[] getCraftDiamondIngredients()
	{
		return new ItemStack[]
		{
				new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.GOLD_INGOT),
				new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.GOLD_INGOT),
				new ItemStack(Items.GOLD_INGOT), new ItemStack(Items.GOLD_INGOT)
		};
	}
	
	public static ItemStack[] getCraftEmeraldIngredients()
	{
		return new ItemStack[]
		{
				new ItemStack(Items.DIAMOND), new ItemStack(Items.DIAMOND), new ItemStack(Items.DIAMOND),
				new ItemStack(Items.DIAMOND), new ItemStack(Items.DIAMOND), new ItemStack(Items.DIAMOND),
				new ItemStack(Items.DIAMOND), new ItemStack(Items.DIAMOND)
		};
	}
}