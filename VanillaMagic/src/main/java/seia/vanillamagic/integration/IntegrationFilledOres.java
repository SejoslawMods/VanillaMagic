package seia.vanillamagic.integration;

import java.lang.reflect.Field;

import net.minecraft.block.Block;

public class IntegrationFilledOres implements IIntegration
{
	public String getModName() 
	{
		return "Filled Ores";
	}
	
	public void postInit() throws Exception
	{
		// Check if we have installed Filled Ores
		Class<?> clazzFilledOres = Class.forName("mod.mcreator.FilledOres");
		
		// Start the right work
		Class<?> clazzRedstone = Class.forName("mod.mcreator.mcreator_fred");
		Field fieldRedstoneOre = clazzRedstone.getField("block"); // BlockFred which extends Block
		Block redstoneOreBlock = (Block) fieldRedstoneOre.get(null); // static field
		CustomOre.INSTANCE.customOreRedstone.add(redstoneOreBlock);	
		
		Class<?> clazzDiamond = Class.forName("mod.mcreator.mcreator_fdiamondD");
		Field fieldDiamondOre = clazzDiamond.getField("block");
		Block diamondOreBlock = (Block) fieldDiamondOre.get(null);
		CustomOre.INSTANCE.customOreDiamond.add(diamondOreBlock);
	}
}