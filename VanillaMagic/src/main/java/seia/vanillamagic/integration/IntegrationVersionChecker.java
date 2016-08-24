package seia.vanillamagic.integration;

import org.apache.logging.log4j.Level;

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
			VanillaMagic.logger.log(Level.INFO, "VersionChecker integration enabled");
		}
		catch(Exception e)
		{
			VanillaMagic.logger.log(Level.WARN, "VersionChecker integration failed");
			e.printStackTrace();
		}
	}
	
	public void postInit() 
	{
	}
}