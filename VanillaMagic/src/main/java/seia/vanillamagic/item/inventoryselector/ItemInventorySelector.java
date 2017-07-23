package seia.vanillamagic.item.inventoryselector;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import seia.vanillamagic.item.CustomItem;

public class ItemInventorySelector extends CustomItem
{
	public void registerRecipe() 
	{
		GameRegistry.addShapelessRecipe(
				new ResourceLocation(""),
				null,
				getItem(), 
				Ingredient.fromStacks(new ItemStack(Items.BLAZE_ROD), new ItemStack(Blocks.CHEST)));
	}
	
	public String getItemName() 
	{
		return "Inventory Selector";
	}
	
	public Item getBaseItem() 
	{
		return Items.BLAZE_ROD;
	}
}