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
import seia.vanillamagic.item.VanillaMagicItems;
import seia.vanillamagic.item.book.BookRegistry;
import seia.vanillamagic.item.enchantedbucket.EnchantedBucketHelper;
import seia.vanillamagic.item.inventoryselector.InventorySelector;
import seia.vanillamagic.item.itemupgrade.ItemUpgradeRegistry;
import seia.vanillamagic.item.potionedcrystal.PotionedCrystalHelper;
import seia.vanillamagic.quest.QuestList;
import seia.vanillamagic.tileentity.TileEntityRegistry;
import seia.vanillamagic.tileentity.machine.quarry.QuarryUpgradeRegistry;

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
	
	public static Logger LOGGER;
	public static ConfigAchievements CONFIG_ACHIEVEMENTS;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		LOGGER = event.getModLog();
		ItemUpgradeRegistry.start();
		CONFIG_ACHIEVEMENTS = new ConfigAchievements(new File(event.getModConfigurationDirectory(), ConfigAchievements.VM_DIRECTORY), event.getSourceFile());
		METADATA = VanillaMagicMetadata.INSTANCE.preInit(METADATA);
		for(int i = 0; i < QuestList.size(); i++)
		{
			QuestHandler.INSTANCE.registerEvent(QuestList.get(i));
		}
		LOGGER.log(Level.INFO, "Registered events: " + QuestHandler.INSTANCE.registeredEvents.size());
		InventorySelector.preInit();
		VanillaMagicDebug.INSTANCE.preInit();
		TileEntityRegistry.preInit();
		ForgeChunkManager.setForcedChunkLoadingCallback(INSTANCE, new ChunkLoadingHandler());
		//WorldHandler.INSTANCE.preInit(); //TODO: Fix World Saving / Loading
		ItemUpgradeRegistry.registerEvents();
		VanillaMagicIntegration.INSTANCE.preInit();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		for(int i = 0; i < QuestList.size(); i++)
		{
			QuestHandler.INSTANCE.addAchievement(QuestList.get(i).getAchievement());
		}
		LOGGER.log(Level.INFO, "Registered achievements: " + QuestHandler.INSTANCE.getAchievements().size());
		VanillaMagicIntegration.INSTANCE.init();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		BookRegistry.postInit();
		CustomTileEntityHandler.INSTANCE.postInit();
		EnchantedBucketHelper.registerFluids();
		PotionedCrystalHelper.registerRecipes();
		VanillaMagicItems.INSTANCE.postInit();
		VanillaMagicIntegration.INSTANCE.postInit();
		LOGGER.log(Level.INFO, "Registered Quarry Upgrades: " + QuarryUpgradeRegistry.countUpgrades());
	}
}