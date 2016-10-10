package seia.vanillamagic.integration;

import java.lang.reflect.Field;

import net.minecraft.block.Block;
import seia.vanillamagic.util.ClassUtils;

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
		Field fieldRedstoneOre = ClassUtils.getField("mod.mcreator.mcreator_fred", "block");
		CustomOre.INSTANCE.customOreRedstone.add((Block) fieldRedstoneOre.get(null)); // static field
		Field fieldDiamondOre = ClassUtils.getField("mod.mcreator.mcreator_fdiamondD", "block");
		CustomOre.INSTANCE.customOreDiamond.add((Block) fieldDiamondOre.get(null));
		
		return true;
	}
}