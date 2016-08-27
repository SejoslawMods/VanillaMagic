package seia.vanillamagic.integration;

import java.lang.reflect.Field;

import net.minecraft.block.Block;

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
		
		Class<?> clazzDenseRedstone = Class.forName("wtf.blocks.redstone.DenseRedstoneOre");
		Field fieldDenseRedstoneOn = clazzDenseRedstone.getField("denseRedstone_on");
		CustomOre.INSTANCE.customOreRedstone.add((Block) fieldDenseRedstoneOn.get(null));
		Field fieldDenseRedstoneOff = clazzDenseRedstone.getField("denseRedstone_off");
		CustomOre.INSTANCE.customOreRedstone.add((Block) fieldDenseRedstoneOff.get(null));
		
		Class<?> clazzRedstoneStalactite = Class.forName("wtf.blocks.redstone.RedstoneStalactite");
		Field fieldDenseRedstoneStalactiteOn = clazzRedstoneStalactite.getField("denseRedstone_on");
		CustomOre.INSTANCE.customOreRedstone.add((Block) fieldDenseRedstoneStalactiteOn.get(null));
		Field fieldDenseRedstoneStalactiteOff = clazzRedstoneStalactite.getField("denseRedstone_off");
		CustomOre.INSTANCE.customOreRedstone.add((Block) fieldDenseRedstoneStalactiteOff.get(null));
		
		return true;
	}
}