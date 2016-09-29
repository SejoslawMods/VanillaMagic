package seia.vanillamagic.integration;

import net.minecraft.block.Block;
import seia.vanillamagic.util.ClassUtils;

public class IntegrationSuperOres implements IIntegration
{
	public String getModName() 
	{
		return "Super Ores";
	}
	
	public boolean postInit() throws Exception
	{
		Class<?> mainClass = Class.forName("abused_master.SuperOres.SuperOres");
		CustomOre.INSTANCE.customOreRedstone.add((Block) ClassUtils.getFieldObject("abused_master.SuperOres.ModBlocks", "blockSuperRedstone", true));
		CustomOre.INSTANCE.customOreDiamond.add((Block) ClassUtils.getFieldObject("abused_master.SuperOres.ModBlocks", "blockSuperDiamond", true));
		
		return true;
	}
}