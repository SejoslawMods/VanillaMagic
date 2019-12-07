package com.github.sejoslaw.vanillamagic.item.liquidsuppressioncrystal;

import net.minecraft.init.Items;
import com.github.sejoslaw.vanillamagic.item.CustomItemCrystal;
import com.github.sejoslaw.vanillamagic.util.CraftingUtil;

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