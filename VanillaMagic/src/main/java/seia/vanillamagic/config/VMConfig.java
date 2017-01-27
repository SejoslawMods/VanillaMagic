package seia.vanillamagic.config;

import java.io.File;

import org.apache.logging.log4j.Level;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.core.VanillaMagic;

public class VMConfig 
{
	private static Configuration _config;
	
	public static String categoryConsole = "Console";
	
	public static String showCustomTileEntitySavingName = "showCustomTileEntitySavingName";
	public static boolean showCustomTileEntitySaving = false;
	
	private VMConfig()
	{
	}
	
	public static void preInit(FMLPreInitializationEvent event) 
	{
		MinecraftForge.EVENT_BUS.register(new VMConfig());
		File configDir = new File(event.getModConfigurationDirectory(), VMConfigAchievements.VM_DIRECTORY);
		if(!configDir.exists())
		{
			configDir.mkdir();
		}
		File configFile = new File(configDir, "VanillaMagic.cfg");
		_config = new Configuration(configFile);
		synchroniseConfig(false);
	}
	
	public static void synchroniseConfig(boolean load) 
	{
		try
		{
			if(load)
			{
				_config.load();
			}
			VMConfig.processConfig();
		}
		catch(Exception e)
		{
			VanillaMagic.LOGGER.log(Level.ERROR, "Error while loading VanillaMagic configuration file.");
			e.printStackTrace();
		}
		finally
		{
			if(_config.hasChanged())
			{
				_config.save();
			}
		}
	}
	
	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent event)
	{
		if(event.getModID().equals(VanillaMagic.MODID))
		{
			VanillaMagic.LOGGER.log(Level.INFO, "Updating config...");
			synchroniseConfig(false);
			init();
			postInit();
		}
	}
	
	public static void processConfig()
	{
		_config.addCustomCategoryComment(categoryConsole, "Options connected with console.");
		showCustomTileEntitySaving = _config.getBoolean(
				showCustomTileEntitySavingName, 
				categoryConsole, 
				showCustomTileEntitySaving, 
				"Should console show when World save / load CustomTileEntity (console spam)");
		
		_config.save();
	}
	
	public static void init() 
	{
	}
	
	public static void postInit() 
	{
	}
}