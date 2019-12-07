package com.github.sejoslaw.vanillamagic.item.inventoryselector;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import com.github.sejoslaw.vanillamagic.item.CustomItem;
import com.github.sejoslaw.vanillamagic.util.CraftingUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ItemInventorySelector extends CustomItem {
	public void registerRecipe() {
		CraftingUtil.addShapelessRecipe(getItem(),
				Ingredient.fromStacks(new ItemStack(Items.BLAZE_ROD), new ItemStack(Blocks.CHEST)));
	}

	public String getItemName() {
		return "Inventory Selector";
	}

	public Item getBaseItem() {
		return Items.BLAZE_ROD;
	}
}