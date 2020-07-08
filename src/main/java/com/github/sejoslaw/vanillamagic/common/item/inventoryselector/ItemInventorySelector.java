package com.github.sejoslaw.vanillamagic.common.item.inventoryselector;

import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.item.CustomItem;
import com.github.sejoslaw.vanillamagic.common.item.CustomItemRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ItemInventorySelector extends CustomItem {
	public void registerRecipe() {
		CustomItemRegistry.addRecipe(this, new ItemStack(Items.BLAZE_ROD), new ItemStack(Blocks.CHEST));
	}

	public ITextComponent getItemName() {
		return TextUtil.wrap("Inventory Selector");
	}

	public Item getBaseItem() {
		return Items.BLAZE_ROD;
	}
}
