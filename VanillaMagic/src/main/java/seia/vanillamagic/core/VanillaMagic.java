package seia.vanillamagic.core;

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
import seia.vanillamagic.api.VanillaMagicAPI;
import seia.vanillamagic.config.VMConfig;
import seia.vanillamagic.config.VMConfigAchievements;
import seia.vanillamagic.creativetab.VanillaMagicCreativeTab;
import seia.vanillamagic.handler.ChunkLoadingHandler;
import seia.vanillamagic.handler.CustomTileEntityHandler;
import seia.vanillamagic.handler.QuestHandler;
import seia.vanillamagic.handler.WorldHandler;
import seia.vanillamagic.integration.VanillaMagicIntegration;
import seia.vanillamagic.item.VanillaMagicItems;
import seia.vanillamagic.item.book.BookRegistry;
import seia.vanillamagic.item.enchantedbucket.EnchantedBucketHelper;
import seia.vanillamagic.item.inventoryselector.InventorySelector;
import seia.vanillamagic.item.potionedcrystal.PotionedCrystalHelper;
import seia.vanillamagic.itemupgrade.ItemUpgradeRegistry;
import seia.vanillamagic.quest.QuestList;
import seia.vanillamagic.quest.mobspawnerdrop.MobSpawnerRegistry;
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
	public static VMConfigAchievements CONFIG_ACHIEVEMENTS;
	
	public static final VanillaMagicCreativeTab CREATIVE_TAB = new VanillaMagicCreativeTab();
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		VanillaMagicAPI.LOGGER.log(Level.INFO, "Starting VanillaMagicAPI from VanillaMagic...");
		LOGGER = event.getModLog();
		VMConfig.preInit(event);
		ItemUpgradeRegistry.start();
		CONFIG_ACHIEVEMENTS = new VMConfigAchievements(new File(event.getModConfigurationDirectory(), VMConfigAchievements.VM_DIRECTORY), event.getSourceFile());
		METADATA = VanillaMagicMetadata.preInit(METADATA);
		for(int i = 0; i < QuestList.size(); ++i)
		{
			QuestHandler.registerEvent(QuestList.get(i));
		}
		LOGGER.log(Level.INFO, "Registered events: " + QuestHandler.REGISTERED_EVENTS.size());
		InventorySelector.preInit();
		VanillaMagicDebug.INSTANCE.preInit();
		TileEntityRegistry.preInit();
		ForgeChunkManager.setForcedChunkLoadingCallback(INSTANCE, new ChunkLoadingHandler());
		WorldHandler.INSTANCE.preInit();
		ItemUpgradeRegistry.registerEvents();
		VanillaMagicIntegration.preInit();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		VMConfig.init();
		for(int i = 0; i < QuestList.size(); ++i)
		{
			QuestHandler.addAchievement(QuestList.get(i).getAchievement());
		}
		LOGGER.log(Level.INFO, "Registered achievements: " + QuestHandler.getAchievements().size());
		VanillaMagicIntegration.init();
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		VMConfig.postInit();
		BookRegistry.postInit();
		CustomTileEntityHandler.postInit();
		EnchantedBucketHelper.registerFluids();
		PotionedCrystalHelper.registerRecipes();
		VanillaMagicItems.postInit();
		VanillaMagicIntegration.postInit();
		LOGGER.log(Level.INFO, "Registered Quarry Upgrades: " + QuarryUpgradeRegistry.countUpgrades());
		MobSpawnerRegistry.init();
	}
}