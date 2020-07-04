package com.github.sejoslaw.vanillamagic.common.item.liquidsuppressioncrystal;

import com.github.sejoslaw.vanillamagic.common.item.CustomItemCrystal;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ItemLiquidSuppressionCrystal extends CustomItemCrystal {
	public void registerRecipe() {
		CraftingUtil.addShapedRecipe(getItem(),
				new Object[] { "BBB", "BNB", "BBB", 'B', Items.BUCKET, 'N', Items.NETHER_STAR });
	}

	public String getItemName() {
		return "Liquid Suppression Crystal";
	}
}