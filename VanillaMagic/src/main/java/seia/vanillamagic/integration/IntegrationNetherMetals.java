package seia.vanillamagic.integration;

import net.minecraft.block.Block;
import seia.vanillamagic.utils.ClassUtils;

public class IntegrationNetherMetals implements IIntegration
{
	public String getModName() 
	{
		return "Nether Metals";
	}
	
	public boolean postInit() throws Exception
	{
		Class<?> mainClass = Class.forName("com.knoxhack.nethermetals.Main");
		CustomOre.INSTANCE.customOreRedstone.add((Block) ClassUtils.getFieldObject("com.knoxhack.nethermetals.blocks.ModBlocks", "netherredstoneOre", true));
		CustomOre.INSTANCE.customOreDiamond.add((Block) ClassUtils.getFieldObject("com.knoxhack.nethermetals.blocks.ModBlocks", "netherdiamondOre", true));
		
		return true;
	}
}