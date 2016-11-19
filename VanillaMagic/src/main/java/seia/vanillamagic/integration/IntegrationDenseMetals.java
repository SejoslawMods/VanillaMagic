package seia.vanillamagic.integration;

import net.minecraft.block.Block;
import seia.vanillamagic.util.ClassUtils;

public class IntegrationDenseMetals implements IIntegration
{
	public String getModName() 
	{
		return "Dense Metals";
	}
	
	public boolean postInit() throws Exception
	{
		Class<?> mainClass = Class.forName("com.mmd.densemetals.Main");
		CustomOre.INSTANCE.customOreRedstone.add((Block) ClassUtils.getFieldObject("com.mmd.densemetals.blocks.ModBlocks", "denseredstoneOre", true));
		CustomOre.INSTANCE.customOreDiamond.add((Block) ClassUtils.getFieldObject("com.mmd.densemetals.blocks.ModBlocks", "densediamondOre", true));
		return true;
	}
}