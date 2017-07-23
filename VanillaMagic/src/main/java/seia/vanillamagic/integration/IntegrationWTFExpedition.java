package seia.vanillamagic.integration;

import net.minecraft.block.Block;
import seia.vanillamagic.util.ClassUtils;

public class IntegrationWTFExpedition implements IIntegration
{
	public String getModName()
	{
		return "WTF-Expedition";
	}
	
	public void postInit() throws Exception
	{
		// Check if we have installed right mod
		Class.forName("wtf.core.Core");
		
		String clazz = "wtf.blocks.redstone.DenseRedstoneOre";
		CustomOre.REDSTONE.add((Block) ClassUtils.getFieldObject(clazz, "denseRedstone_on", true));
		CustomOre.REDSTONE.add((Block) ClassUtils.getFieldObject(clazz, "denseRedstone_off", true));
		
		clazz = "wtf.blocks.redstone.RedstoneStalactite";
		CustomOre.REDSTONE.add((Block) ClassUtils.getFieldObject(clazz, "denseRedstone_on", true));
		CustomOre.REDSTONE.add((Block) ClassUtils.getFieldObject(clazz, "denseRedstone_off", true));
	}
}