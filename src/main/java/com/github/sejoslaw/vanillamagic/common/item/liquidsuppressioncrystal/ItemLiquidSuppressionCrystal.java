package com.github.sejoslaw.vanillamagic.common.item.liquidsuppressioncrystal;

import com.github.sejoslaw.vanillamagic.common.item.CustomItemCrystal;
import com.github.sejoslaw.vanillamagic.common.item.CustomItemRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ItemLiquidSuppressionCrystal extends CustomItemCrystal {
	public void registerRecipe() {
		CustomItemRegistry.addRecipe(this, new ItemStack(Items.BUCKET, 8), new ItemStack(Items.NETHER_STAR));
	}

	public ITextComponent getItemName() {
		return new StringTextComponent("Liquid Suppression Crystal");
	}
}