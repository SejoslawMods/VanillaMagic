package com.github.sejoslaw.vanillamagic.integration;

import java.lang.reflect.Field;

import net.minecraft.block.Block;
import com.github.sejoslaw.vanillamagic.util.ClassUtils;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class IntegrationFilledOres implements IIntegration {
	public String getModName() {
		return "Filled Ores";
	}

	public void postInit() throws Exception {
		Class.forName("mod.mcreator.FilledOres");

		Field fieldRedstoneOre = ClassUtils.getField("mod.mcreator.mcreator_fred", "block");
		CustomOre.REDSTONE.add((Block) fieldRedstoneOre.get(null));
		Field fieldDiamondOre = ClassUtils.getField("mod.mcreator.mcreator_fdiamondD", "block");
		CustomOre.DIAMOND.add((Block) fieldDiamondOre.get(null));
	}
}