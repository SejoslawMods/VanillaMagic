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
import seia.vanillamagic.api.quest.QuestList;
import seia.vanillamagic.api.upgrade.toolupgrade.ToolRegistry;
import seia.vanillamagic.config.VMConfig;
import seia.vanillamagic.config.VMConfigAchievements;
import seia.vanillamagic.handler.ChunkLoadingHandler;
import seia.vanillamagic.handler.CustomTileEntityHandler;
import seia.vanillamagic.handler.PlayerEventHandler;
import seia.vanillamagic.handler.WorldHandler;
import seia.vanillamagic.integration.VanillaMagicIntegration;
import seia.vanillamagic.item.VanillaMagicItems;
import seia.vanillamagic.item.book.BookRegistry;
import seia.vanillamagic.item.enchantedbucket.EnchantedBucketHelper;
import seia.vanillamagic.item.inventoryselector.InventorySelector;
import seia.vanillamagic.item.potionedcrystal.PotionedCrystalHelper;
import seia.vanillamagic.magic.spell.SpellRegistry;
import seia.vanillamagic.magic.wand.WandRegistry;
import seia.vanillamagic.quest.mobspawnerdrop.MobSpawnerRegistry;
import seia.vanillamagic.quest.upgrade.itemupgrade.ItemUpgradeRegistry;
import seia.vanillamagic.questbook.EventQuestBook;
import seia.vanillamagic.tileentity.TileEntityRegistry;
import seia.vanillamagic.tileentity.machine.quarry.QuarryUpgradeRegistry;
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
	 * Config used for loading achievements
	 */
	public static VMConfigAchievements CONFIG_ACHIEVEMENTS;
	
	/**
	 * VM custom Creative Tab
	 */
	public static final VanillaMagicCreativeTab CREATIVE_TAB = new VanillaMagicCreativeTab();
	
	
	/**
	 * PreInitialization stage.
	 */
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		VanillaMagicAPI.LOGGER.log(Level.INFO, "Starting VanillaMagicAPI from VanillaMagic...");
		LOGGER = event.getModLog();
		METADATA = VanillaMagicMetadata.preInit(METADATA);
		EventUtil.registerEvent(new EventQuestBook()); // Main Questbook Event
		VMConfig.preInit(event);
		ToolRegistry.preInit();
		ItemUpgradeRegistry.preInit();
		WandRegistry.preInit();
		SpellRegistry.preInit();
		CONFIG_ACHIEVEMENTS = new VMConfigAchievements(new File(event.getModConfigurationDirectory(), VMConfigAchievements.VM_DIRECTORY), event.getSourceFile());
		for (int i = 0; i < QuestList.size(); ++i) EventUtil.registerEvent(QuestList.get(i));
		LOGGER.log(Level.INFO, "Registered Quests: " + QuestList.size());
		PlayerEventHandler.preInit();
		InventorySelector.preInit();
		VanillaMagicDebug.INSTANCE.preInit();
		TileEntityRegistry.preInit();
		ForgeChunkManager.setForcedChunkLoadingCallback(INSTANCE, new ChunkLoadingHandler());
		WorldHandler.INSTANCE.preInit();
		ItemUpgradeRegistry.registerEvents();
		VanillaMagicIntegration.preInit(); // Integration should be read ALWAYS at the end.
	}
	
	/**
	 * Initialization stage.
	 */
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		VMConfig.init();
		VanillaMagicIntegration.init(); // Integration should be read ALWAYS at the end.
	}
	
	/**
	 * PostInitialization stage.
	 */
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
		MobSpawnerRegistry.postInit(); // Integration should be read ALWAYS at the end.
	}
	
	/**
	 * Print log to console.
	 */
	public static void log(Level level, String log)
	{
		LOGGER.log(level, log);
	}
}