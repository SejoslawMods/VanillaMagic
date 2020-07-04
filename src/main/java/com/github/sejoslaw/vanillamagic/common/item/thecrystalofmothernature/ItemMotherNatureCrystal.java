package com.github.sejoslaw.vanillamagic.common.item.thecrystalofmothernature;

import com.github.sejoslaw.vanillamagic.common.item.CustomItemCrystal;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;

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
