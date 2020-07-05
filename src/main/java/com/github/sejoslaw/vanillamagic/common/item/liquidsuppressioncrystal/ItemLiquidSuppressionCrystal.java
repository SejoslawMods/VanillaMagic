package com.github.sejoslaw.vanillamagic.common.item.liquidsuppressioncrystal;

import com.github.sejoslaw.vanillamagic.common.item.CustomItemCrystal;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ItemLiquidSuppressionCrystal extends CustomItemCrystal {
	public void registerRecipe() {
		CraftingUtil.addShapedRecipe(getItem(),
				new Object[] { "BBB", "BNB", "BBB", 'B', Items.BUCKET, 'N', Items.NETHER_STAR });
	}

	public ITextComponent getItemName() {
		return new StringTextComponent("Liquid Suppression Crystal");
	}
}