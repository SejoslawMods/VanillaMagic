package seia.vanillamagic.integration;

import net.minecraftforge.fml.common.event.FMLInterModComms;
import seia.vanillamagic.VanillaMagic;

public class IntegrationVersionChecker implements IIntegration
{
	public String getModName()
	{
		return "VersionChecker";
	}
	
	public boolean init() throws Exception
	{
		// Version Checker integration
		VanillaMagicIntegration.INSTANCE.tagCompound.setString("curseProjectName", "vanilla-magic");
		VanillaMagicIntegration.INSTANCE.tagCompound.setString("curseFilenameParser", VanillaMagic.MODID + "-[].jar");
		FMLInterModComms.sendRuntimeMessage(VanillaMagic.MODID, "VersionChecker", "addCurseCheck", VanillaMagicIntegration.INSTANCE.tagCompound);
		
		return true;
	}
}