package seia.vanillamagic.integration;

import net.minecraft.block.Block;
import seia.vanillamagic.util.ClassUtils;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class IntegrationNetherMetals implements IIntegration {
	public String getModName() {
		return "Nether Metals";
	}

	public void postInit() throws Exception {
		Class.forName("com.knoxhack.nethermetals.Main");

		CustomOre.REDSTONE.add((Block) ClassUtils.getFieldObject("com.knoxhack.nethermetals.blocks.ModBlocks",
				"netherredstoneOre", true));
		CustomOre.DIAMOND.add((Block) ClassUtils.getFieldObject("com.knoxhack.nethermetals.blocks.ModBlocks",
				"netherdiamondOre", true));
	}
}