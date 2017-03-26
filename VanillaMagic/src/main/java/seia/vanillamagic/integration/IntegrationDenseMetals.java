package seia.vanillamagic.integration;

import net.minecraft.block.Block;
import seia.vanillamagic.util.ClassUtils;

public class IntegrationDenseMetals implements IIntegration
{
	public String getModName() 
	{
		return "Dense Metals";
	}
	
	public void postInit() throws Exception
	{
		Class<?> mainClass = Class.forName("com.mmd.densemetals.Main");
		CustomOre.REDSTONE.add((Block) ClassUtils.getFieldObject("com.mmd.densemetals.blocks.ModBlocks", 
				"denseredstoneOre", true));
		CustomOre.DIAMOND.add((Block) ClassUtils.getFieldObject("com.mmd.densemetals.blocks.ModBlocks", 
				"densediamondOre", true));
	}
}