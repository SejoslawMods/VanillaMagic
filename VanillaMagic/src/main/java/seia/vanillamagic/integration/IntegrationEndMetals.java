package seia.vanillamagic.integration;

import net.minecraft.block.Block;
import seia.vanillamagic.util.ClassUtils;

public class IntegrationEndMetals implements IIntegration
{
	public String getModName() 
	{
		return "End Metals";
	}
	
	public void postInit() throws Exception
	{
		Class<?> mainClass = Class.forName("endmetals.Main");
		CustomOre.REDSTONE.add((Block) ClassUtils.getFieldObject("endmetals.blocks.ModBlocks", 
				"endredstoneOre", true));
		CustomOre.DIAMOND.add((Block) ClassUtils.getFieldObject("endmetals.blocks.ModBlocks", 
				"enddiamondOre", true));
	}
}