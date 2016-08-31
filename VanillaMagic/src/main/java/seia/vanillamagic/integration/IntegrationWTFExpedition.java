package seia.vanillamagic.integration;

import net.minecraft.block.Block;
import seia.vanillamagic.utils.ClassUtils;

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
		
//		Class<?> clazzDenseRedstone = Class.forName("wtf.blocks.redstone.DenseRedstoneOre");
		String clazz = "wtf.blocks.redstone.DenseRedstoneOre";
//		Field fieldDenseRedstoneOn = clazzDenseRedstone.getField("denseRedstone_on");
//		CustomOre.INSTANCE.customOreRedstone.add((Block) fieldDenseRedstoneOn.get(null));
		CustomOre.INSTANCE.customOreRedstone.add((Block) ClassUtils.getFieldObject(clazz, "denseRedstone_on", true));
//		Field fieldDenseRedstoneOff = clazzDenseRedstone.getField("denseRedstone_off");
//		CustomOre.INSTANCE.customOreRedstone.add((Block) fieldDenseRedstoneOff.get(null));
		CustomOre.INSTANCE.customOreRedstone.add((Block) ClassUtils.getFieldObject(clazz, "denseRedstone_off", true));
		
//		Class<?> clazzRedstoneStalactite = Class.forName("wtf.blocks.redstone.RedstoneStalactite");
		clazz = "wtf.blocks.redstone.RedstoneStalactite";
//		Field fieldDenseRedstoneStalactiteOn = clazzRedstoneStalactite.getField("denseRedstone_on");
//		CustomOre.INSTANCE.customOreRedstone.add((Block) fieldDenseRedstoneStalactiteOn.get(null));
		CustomOre.INSTANCE.customOreRedstone.add((Block) ClassUtils.getFieldObject(clazz, "denseRedstone_on", true));
//		Field fieldDenseRedstoneStalactiteOff = clazzRedstoneStalactite.getField("denseRedstone_off");
//		CustomOre.INSTANCE.customOreRedstone.add((Block) fieldDenseRedstoneStalactiteOff.get(null));
		CustomOre.INSTANCE.customOreRedstone.add((Block) ClassUtils.getFieldObject(clazz, "denseRedstone_off", true));
		
		return true;
	}
}