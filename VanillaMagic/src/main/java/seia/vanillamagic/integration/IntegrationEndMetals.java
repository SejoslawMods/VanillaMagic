package seia.vanillamagic.integration;

import net.minecraft.block.Block;
import seia.vanillamagic.utils.ClassUtils;

public class IntegrationEndMetals implements IIntegration
{
	public String getModName() 
	{
		return "End Metals";
	}
	
	public boolean postInit() throws Exception
	{
		Class<?> mainClass = Class.forName("endmetals.Main");
		CustomOre.INSTANCE.customOreRedstone.add((Block) ClassUtils.getFieldObject("endmetals.blocks.ModBlocks", "endredstoneOre", true));
		CustomOre.INSTANCE.customOreDiamond.add((Block) ClassUtils.getFieldObject("endmetals.blocks.ModBlocks", "enddiamondOre", true));
		
		return true;
	}
}