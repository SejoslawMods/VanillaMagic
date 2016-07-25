package seia.vanillamagic;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.event.FMLInterModComms;

public class VanillaMagicIntegration 
{
	public static final VanillaMagicIntegration INSTANCE = new VanillaMagicIntegration();
	
	//==================================================================================================
	
	public final NBTTagCompound nbtTagCompound = new NBTTagCompound();
	
	public void preInit()
	{
	}
	
	public void init()
	{
		try
		{
			// Version Checker integration
			nbtTagCompound.setString("curseProjectName", "vanilla-magic");
			nbtTagCompound.setString("curseFilenameParser", VanillaMagic.MODID + "-[].jar");
			FMLInterModComms.sendRuntimeMessage(VanillaMagic.MODID, "VersionChecker", "addCurseCheck", nbtTagCompound);
		}
		catch(Exception e)
		{
			System.out.println("Version Checker integration failed.");
			e.printStackTrace();
		}
	}
	
	public void postInit()
	{
	}
}