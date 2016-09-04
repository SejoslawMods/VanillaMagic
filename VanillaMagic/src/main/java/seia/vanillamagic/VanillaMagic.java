package seia.vanillamagic;

import java.io.File;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import seia.vanillamagic.config.achievement.ConfigAchievements;
import seia.vanillamagic.handler.ChunkLoadingHandler;
import seia.vanillamagic.handler.QuestHandler;
import seia.vanillamagic.handler.customtileentity.CustomTileEntityHandler;
import seia.vanillamagic.integration.VanillaMagicIntegration;
import seia.vanillamagic.items.VanillaMagicItems;
import seia.vanillamagic.items.book.BookRegistry;
import seia.vanillamagic.items.enchantedbucket.EnchantedBucketHelper;
import seia.vanillamagic.items.potionedcrystal.PotionedCrystalHelper;
import seia.vanillamagic.quest.QuestList;

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
	public static ModMetadata modMetadata;
	
	public static Logger logger;
	public static ConfigAchievements configAchievements;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		logger = event.getModLog();
		configAchievements = new ConfigAchievements(new File(event.getModConfigurationDirectory(), ConfigAchievements.VM_DIRECTORY), event.getSourceFile());
		modMetadata = VanillaMagicMetadata.INSTANCE.preInit(modMetadata);
		for(int i = 0; i < QuestList.QUESTS.size(); i++)
		{
			QuestHandler.INSTANCE.registerEvent(QuestList.QUESTS.get(i));
		}
		logger.log(Level.INFO, "Registered events: " + QuestHandler.INSTANCE.registeredEvents.size());
		VanillaMagicDebug.INSTANCE.preInit();
		VanillaMagicRegistry.INSTANCE.preInit();
		ForgeChunkManager.setForcedChunkLoadingCallback(INSTANCE, new ChunkLoadingHandler());
		//WorldHandler.INSTANCE.preInit(); //TODO: FIX
		VanillaMagicIntegration.INSTANCE.preInit();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		for(int i = 0; i < QuestList.QUESTS.size(); i++)
		{
			QuestHandler.INSTANCE.addAchievement(QuestList.QUESTS.get(i).achievement);
		}
		logger.log(Level.INFO, "Registered achievements: " + QuestHandler.INSTANCE.getAchievements().size());
		VanillaMagicIntegration.INSTANCE.init();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		BookRegistry.INSTANCE.postInit(); // TODO:
		CustomTileEntityHandler.INSTANCE.postInit();
		EnchantedBucketHelper.registerFluids();
		PotionedCrystalHelper.registerRecipes();
		VanillaMagicItems.INSTANCE.postInit();
		VanillaMagicIntegration.INSTANCE.postInit();
	}
}