package com.github.sejoslaw.vanillamagic.item.thecrystalofmothernature;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import com.github.sejoslaw.vanillamagic.item.CustomItemCrystal;
import com.github.sejoslaw.vanillamagic.util.CraftingUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ItemMotherNatureCrystal extends CustomItemCrystal {
	public void registerRecipe() {
		CraftingUtil.addShapedRecipe(getItem(), new Object[] { "MSM", "SNS", "PSP", 'M', Items.MELON, 'S',
				Items.WHEAT_SEEDS, 'N', Items.NETHER_STAR, 'P', Blocks.PUMPKIN });
	}

	public String getItemName() {
		return "The Crystal of Mother Nature";
	}
}