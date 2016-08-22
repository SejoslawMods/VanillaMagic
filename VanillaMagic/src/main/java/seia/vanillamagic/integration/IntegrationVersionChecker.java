package seia.vanillamagic.integration;

import net.minecraftforge.fml.common.event.FMLInterModComms;
import seia.vanillamagic.VanillaMagic;

public class IntegrationVersionChecker implements IIntegration
{
	public void preInit() 
	{
	}
	
	public void init() 
	{
		try
		{
			// Version Checker integration
			VanillaMagicIntegration.INSTANCE.tagCompound.setString("curseProjectName", "vanilla-magic");
			VanillaMagicIntegration.INSTANCE.tagCompound.setString("curseFilenameParser", VanillaMagic.MODID + "-[].jar");
			FMLInterModComms.sendRuntimeMessage(VanillaMagic.MODID, "VersionChecker", "addCurseCheck", VanillaMagicIntegration.INSTANCE.tagCompound);
			System.out.println("VersionChecker integration enabled");
		}
		catch(Exception e)
		{
			System.out.println("Version Checker integration failed");
			e.printStackTrace();
		}
	}
	
	public void postInit() 
	{
	}
}