package com.github.sejoslaw.vanillamagic.common.item.accelerationcrystal;

import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.item.CustomItemCrystal;
import com.github.sejoslaw.vanillamagic.common.util.CraftingUtil;
import net.minecraft.util.text.ITextComponent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ItemAccelerationCrystal extends CustomItemCrystal {
	public void registerRecipe() {
		CraftingUtil.addShapedRecipe(getItem(), new Object[] { " B ", "BNB", " B ", 'B', Items.BOOK, 'N', Items.NETHER_STAR });
	}

	public ITextComponent getItemName() {
		return TextUtil.wrap("Acceleration Crystal");
	}
}