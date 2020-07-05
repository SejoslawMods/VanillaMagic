package com.github.sejoslaw.vanillamagic.common.item.inventoryselector;

import com.github.sejoslaw.vanillamagic.common.item.CustomItem;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;

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
