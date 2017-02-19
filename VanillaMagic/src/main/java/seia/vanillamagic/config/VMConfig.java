package seia.vanillamagic.config;

import java.io.File;

import org.apache.logging.log4j.Level;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.core.VanillaMagic;

public class VMConfig 
{
	private static Configuration _config;
	
	// Console
	private static final String _categoryConsole = "Console";
	public static boolean showCustomTileEntitySaving = false;
	
	// Player
	private static final String _categoryPlayer = "Player";
	public static boolean givePlayerCustomBooksOnLoggedIn = true;
	
	// Machine
	private static final String _categoryMachine = "Machine";
	public static int tileSpeedyTicks = 1000;
	public static int tileSpeedySize = 4;
	public static int tileMachineOneOperationCost = 100;
	public static int tileMachineMaxTicks = 4000;
	
	// Item
	private static final String _categoryItem = "Item";
	public static int accelerationCrystalUpdateTicks = 100;
	public static int liquidSuppressionCrystalRadius = 5;
	public static int motherNatureCrystalRange = 10;
	public static int itemMagnetRange = 6;
	public static int itemMagnetMaxPulledItems = 200;
	
	// Spell
	private static final String _categorySpell = "Spell";
	public static int spellCostSummonFriendly = 32;
	public static int spellCostSummonHostile = 8;
	public static boolean enableMoveBlockBlacklist = true;
	
	// Meteor
	private static final String _categoryMeteor = "Meteor";
	public static float basicMeteorSize = 5.0f;
	public static int basicMeteorExplosionPower = 25;
	public static float explosionDropRate = 0.1f;
	
	// Hostile Mobs
	private static final String _categoryHostileMobs = "Hostile Mobs";
	public static int percentForSpawnWithArmor = 10;
	public static int percentForSpawnOnHorse = 15;
	
	// Evoker Crystal
	private static final String _categoryEvokerCrystal = "Evoker Crystal";
	public static int vexNumber = 3;
	public static boolean vexHasLimitedLife = true;
	
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
		// Console
		_config.addCustomCategoryComment(_categoryConsole, "Options connected with Console.");
		showCustomTileEntitySaving = _config.getBoolean(
				"showCustomTileEntitySaving", 
				_categoryConsole, 
				showCustomTileEntitySaving, 
				"Should console show when World save / load CustomTileEntity (console spam).");
		
		// Player
		_config.addCustomCategoryComment(_categoryPlayer, "Options connected with Player");
		givePlayerCustomBooksOnLoggedIn = _config.getBoolean(
				"givePlayerCustomBooksOnLoggedIn", 
				_categoryPlayer, 
				givePlayerCustomBooksOnLoggedIn, 
				"Should Player get Vanilla Magic custom books on spawn (logged in).");
		
		// Machine
		_config.addCustomCategoryComment(_categoryMachine, "Options connected with Vanilla Magic Machines.");
		tileSpeedyTicks = _config.getInt(
				"tileSpeedyTicks", 
				_categoryMachine, 
				tileSpeedyTicks, 
				1, 
				Integer.MAX_VALUE, 
				"The number of ticks in 1 Minecraft tick that Speedy can do to a single block.");
		tileSpeedySize = _config.getInt(
				"tileSpeedySize", 
				_categoryMachine, 
				tileSpeedySize, 
				1, 
				Integer.MAX_VALUE, 
				"Size of the Speedy - Area on which Speedy can operate.");
		tileMachineOneOperationCost = _config.getInt(
				"tileMachineOneOperationCost", 
				_categoryMachine, 
				tileMachineOneOperationCost, 
				1, 
				Integer.MAX_VALUE, 
				"Cost of a single Machine operation.");
		tileMachineMaxTicks = _config.getInt(
				"tileMachineMaxTicks", 
				_categoryMachine, 
				tileMachineMaxTicks, 
				tileMachineMaxTicks, 
				Integer.MAX_VALUE, 
				"Max ticks (internal fuel) that Machine can store.");
		
		// Item
		_config.addCustomCategoryComment(_categoryItem, "Options connected with Vanilla Magic Items.");
		accelerationCrystalUpdateTicks = _config.getInt(
				"accelerationCrystalUpdateTicks", 
				_categoryItem, 
				accelerationCrystalUpdateTicks, 
				1, 
				Integer.MAX_VALUE, 
				"The number of ticks that should be ticked when Player holds Acceleration Crystal.");
		liquidSuppressionCrystalRadius = _config.getInt(
				"liquidSuppressionCrystalRadius", 
				_categoryItem, 
				liquidSuppressionCrystalRadius, 
				1, 
				Integer.MAX_VALUE, 
				"Radius on which Liquid Suppression Crystal works (in blocks).");
		motherNatureCrystalRange = _config.getInt(
				"motherNatureCrystalRange", 
				_categoryItem, 
				motherNatureCrystalRange, 
				1, 
				Integer.MAX_VALUE, 
				"Range on which Mother Nature Crystal will work.");
		itemMagnetRange = _config.getInt(
				"itemMagnetRange", 
				_categoryItem, 
				itemMagnetRange, 
				1, 
				Integer.MAX_VALUE, 
				"Range on which Item Magnet will work.");
		itemMagnetMaxPulledItems = _config.getInt(
				"itemMagnetMaxPulledItems", 
				_categoryItem, 
				itemMagnetMaxPulledItems, 
				1, 
				Integer.MAX_VALUE, 
				"The maximum number of items that Item Magnet can pull at once.");
		
		// Spell
		_config.addCustomCategoryComment(_categorySpell, "Options connected with Vanilla Magic Spells.");
		spellCostSummonFriendly = _config.getInt(
				"spellCostSummonFriendly", 
				_categorySpell, 
				spellCostSummonFriendly, 
				1, 
				64, 
				"Cost of one friendly mob spawn (in items from hand).");
		spellCostSummonHostile = _config.getInt(
				"spellCostSummonHostile", 
				_categorySpell, 
				spellCostSummonHostile, 
				1, 
				64, 
				"Cost of one hostile mob spawn (in items from hand).");
		enableMoveBlockBlacklist = _config.getBoolean(
				"enableMoveBlockBlacklist", 
				_categorySpell, 
				enableMoveBlockBlacklist, 
				"Should Blacklist for Quest Move Block By Book be enabled.");
		
		// Meteor
		_config.addCustomCategoryComment(_categoryMeteor, "Options connected with Meteor Explosion.");
		basicMeteorSize = _config.getFloat(
				"basicMeteorSize", 
				_categoryMeteor, 
				basicMeteorSize, 
				1.0f, 
				Float.MAX_VALUE, 
				"Basic size of the Meteor.");
		basicMeteorExplosionPower = _config.getInt(
				"basicMeteorExplosionPower", 
				_categoryMeteor, 
				basicMeteorExplosionPower, 
				1, 
				Integer.MAX_VALUE, 
				"Meteor explosion power.");
		explosionDropRate = _config.getFloat(
				"explosionDropRate", 
				_categoryMeteor, 
				explosionDropRate, 
				0.0f, 
				1.0f, 
				"Rate between 0-1 for block drops from explosion.");
		
		// Hostile Mobs
		_config.addCustomCategoryComment(_categoryHostileMobs, "Options connected with Hostile Mobs Spawning with Spell.");
		percentForSpawnWithArmor = _config.getInt(
				"percentForSpawnWithArmor", 
				_categoryHostileMobs, 
				percentForSpawnWithArmor, 
				1, 
				100, 
				"Percent with which there is a chance for spawning a Mob with Armor (if possible for Armor).");
		percentForSpawnOnHorse = _config.getInt(
				"percentForSpawnOnHorse", 
				_categoryHostileMobs, 
				percentForSpawnOnHorse, 
				1, 
				100, 
				"Percent with which there is a chance for spawning a Mob on Horse (if Mob has equal Horse).");
		
		// Evoker Crystal
		_config.addCustomCategoryComment(_categoryEvokerCrystal, "Options connected with Evoker Crystal.");
		vexNumber = _config.getInt(
				"vexNumber",
				_categoryEvokerCrystal,
				vexNumber,
				1,
				Integer.MAX_VALUE,
				"The number of Vex's that can be spawn using Evoker Crystal."
				);
		vexHasLimitedLife = _config.getBoolean(
				"vexHasLimitedLife", 
				_categoryEvokerCrystal, 
				vexHasLimitedLife, 
				"Should spawned Vex have limited life.");
		
		_config.save();
	}
	
	public static void init() 
	{
	}
	
	public static void postInit() 
	{
	}
}