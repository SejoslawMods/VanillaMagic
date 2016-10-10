package seia.vanillamagic.integration;

import net.minecraft.block.Block;
import seia.vanillamagic.util.ClassUtils;

public class IntegrationWTFExpedition implements IIntegration
{
	public String getModName()
	{
		return "WTF-Expedition";
	}
	
	public boolean postInit() throws Exception
	{
		// Check if we have installed right mod
		Class<?> clazzCore = Class.forName("wtf.core.Core");
		
		String clazz = "wtf.blocks.redstone.DenseRedstoneOre";
		CustomOre.INSTANCE.customOreRedstone.add((Block) ClassUtils.getFieldObject(clazz, "denseRedstone_on", true));
		CustomOre.INSTANCE.customOreRedstone.add((Block) ClassUtils.getFieldObject(clazz, "denseRedstone_off", true));
		
		clazz = "wtf.blocks.redstone.RedstoneStalactite";
		CustomOre.INSTANCE.customOreRedstone.add((Block) ClassUtils.getFieldObject(clazz, "denseRedstone_on", true));
		CustomOre.INSTANCE.customOreRedstone.add((Block) ClassUtils.getFieldObject(clazz, "denseRedstone_off", true));
		
		return true;
	}
}