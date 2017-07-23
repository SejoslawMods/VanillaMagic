package seia.vanillamagic.integration;

import net.minecraft.block.Block;
import seia.vanillamagic.util.ClassUtils;

public class IntegrationSuperOres implements IIntegration
{
	public String getModName() 
	{
		return "Super Ores";
	}
	
	public void postInit() throws Exception
	{
		Class.forName("abused_master.SuperOres.SuperOres");
		CustomOre.REDSTONE.add((Block) ClassUtils.getFieldObject("abused_master.SuperOres.ModBlocks", 
				"blockSuperRedstone", true));
		CustomOre.DIAMOND.add((Block) ClassUtils.getFieldObject("abused_master.SuperOres.ModBlocks", 
				"blockSuperDiamond", true));
	}
}