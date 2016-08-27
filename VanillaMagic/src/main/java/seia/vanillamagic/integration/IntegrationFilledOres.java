package seia.vanillamagic.integration;

import java.lang.reflect.Field;

import net.minecraft.block.Block;

public class IntegrationFilledOres implements IIntegration
{
	public String getModName() 
	{
		return "Filled Ores";
	}
	
	public boolean postInit() throws Exception
	{
		// Check if we have installed Filled Ores
		Class<?> clazzFilledOres = Class.forName("mod.mcreator.FilledOres");
		
		// Start the right work
		Class<?> clazzRedstone = Class.forName("mod.mcreator.mcreator_fred");
		Field fieldRedstoneOre = clazzRedstone.getField("block"); // BlockFred which extends Block
		CustomOre.INSTANCE.customOreRedstone.add((Block) fieldRedstoneOre.get(null)); // static field
		
		Class<?> clazzDiamond = Class.forName("mod.mcreator.mcreator_fdiamondD");
		Field fieldDiamondOre = clazzDiamond.getField("block");
		CustomOre.INSTANCE.customOreDiamond.add((Block) fieldDiamondOre.get(null));
		
		return true;
	}
}