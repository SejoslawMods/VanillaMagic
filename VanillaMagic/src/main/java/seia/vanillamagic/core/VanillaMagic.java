package seia.vanillamagic.core;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import seia.vanillamagic.api.VanillaMagicAPI;
import seia.vanillamagic.api.quest.QuestList;
import seia.vanillamagic.quest.DummyQuest;
import seia.vanillamagic.questbook.QuestBookEvent;
import seia.vanillamagic.util.EventUtil;

/**
 * Core mod file.
 */
@Mod(
		modid = VanillaMagic.MODID, 
		version = VanillaMagic.VERSION, 
		name = VanillaMagic.NAME
)
public class VanillaMagic 
{
	public static final String MODID = "vanillamagic";
	public static final String VERSION = "@VERSION@";
	public static final String NAME = "Vanilla Magic";
	
	@Mod.Instance
	public static VanillaMagic INSTANCE;
	
	@Mod.Metadata
	public static ModMetadata METADATA;
	
	/**
	 * Mod internal logger
	 */
	public static Logger LOGGER;
	
	/**
	 * PreInitialization stage.
	 */
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		VanillaMagicAPI.LOGGER.log(Level.INFO, "Starting VanillaMagicAPI from VanillaMagic...");
		LOGGER = event.getModLog();
		METADATA = VanillaMagicMetadata.preInit(METADATA);
		EventUtil.registerEvent(new QuestBookEvent()); // Main Questbook Event
		
		// TODO: Testing DummyQuest
		QuestList.addQuest(new DummyQuest());
	}
	
	/**
	 * Initialization stage.
	 */
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
	}
	
	/**
	 * PostInitialization stage.
	 */
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
	}
	
	/**
	 * Print log to console.
	 */
	public static void log(Level level, String log)
	{
		LOGGER.log(level, log);
	}
}