package seia.vanillamagic.item.accelerationcrystal;

import net.minecraft.init.Items;
import seia.vanillamagic.item.CustomItemCrystal;
import seia.vanillamagic.util.CraftingUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ItemAccelerationCrystal extends CustomItemCrystal {
	public void registerRecipe() {
		CraftingUtil.addShapedRecipe(getItem(),
				new Object[] { " B ", "BNB", " B ", 'B', Items.BOOK, 'N', Items.NETHER_STAR });
	}

	public String getItemName() {
		return "Acceleration Crystal";
	}
}