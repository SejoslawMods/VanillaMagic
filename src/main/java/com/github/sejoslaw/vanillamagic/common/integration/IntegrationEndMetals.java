package com.github.sejoslaw.vanillamagic.integration;

import net.minecraft.block.Block;
import com.github.sejoslaw.vanillamagic.util.ClassUtils;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class IntegrationEndMetals implements IIntegration {
	public String getModName() {
		return "End Metals";
	}

	public void postInit() throws Exception {
		Class.forName("endmetals.Main");
		CustomOre.REDSTONE.add((Block) ClassUtils.getFieldObject("endmetals.blocks.ModBlocks", "endredstoneOre", true));
		CustomOre.DIAMOND.add((Block) ClassUtils.getFieldObject("endmetals.blocks.ModBlocks", "enddiamondOre", true));
	}
}