package seia.vanillamagic.item.inventoryselector;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import seia.vanillamagic.item.CustomItem;

public class ItemInventorySelector extends CustomItem
{
	public void registerRecipe() 
	{
		GameRegistry.addShapelessRecipe(getItem(), Items.BLAZE_ROD, Blocks.CHEST);
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