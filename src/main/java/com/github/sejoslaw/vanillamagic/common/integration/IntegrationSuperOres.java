package com.github.sejoslaw.vanillamagic.integration;

import net.minecraft.block.Block;
import com.github.sejoslaw.vanillamagic.util.ClassUtils;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class IntegrationSuperOres implements IIntegration {
	public String getModName() {
		return "Super Ores";
	}

	public void postInit() throws Exception {
		Class.forName("abused_master.SuperOres.SuperOres");

		CustomOre.REDSTONE.add(
				(Block) ClassUtils.getFieldObject("abused_master.SuperOres.ModBlocks", "blockSuperRedstone", true));
		CustomOre.DIAMOND
				.add((Block) ClassUtils.getFieldObject("abused_master.SuperOres.ModBlocks", "blockSuperDiamond", true));
	}
}