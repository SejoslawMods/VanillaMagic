package seia.vanillamagic.config;

import java.io.File;

import org.apache.logging.log4j.Level;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.core.VanillaMagic;

/**
 * VM Forge Configuration File
 */
public class VMConfig 
{
	private static Configuration _config;
	
	// Console
	private static final String _CATEGORY_CONSOLE = "Console";
	public static boolean SHOW_CUSTOM_TILE_ENTITY_SAVING = false;
	
	// Player
	private static final String _CATEGORY_PLAYER = "Player";
	public static boolean GIVE_PLAYER_CUSTOM_BOOKS_ON_LOGIN = true;
	
	// Machine
	private static final String _CATEGORY_MACHINE = "Machine";
	public static int TILE_SPEEDY_TICKS = 1000;
	public static int TILE_SPEEDY_SIZE = 4;
	public static int TILE_MACHINE_ONE_OPERATION_COST = 100;
	public static int TILE_MACHINE_MAX_TICKS = 4000;
	
	// Item
	private static final String _CATEGORY_ITEM = "Item";
	public static int ACCELERATION_CRYSTAL_UPDATE_TICKS = 100;
	public static int LIQUID_SUPPRESSION_CRYSTAL_RADIUS = 5;
	public static int MOTHER_NATURE_CRYSTAL_RANGE = 10;
	public static int ITEM_MAGNET_RANGE = 6;
	public static int ITEM_MAGNET_PULLED_ITEMS = 200;
	public static boolean ITEM_CAN_AUTOPLANT = true;
	
	// Spell
	private static final String _CATEGORY_SPELL = "Spell";
	public static int SPELL_COST_SUMMON_FRIENDLY = 32;
	public static int SPELL_COST_SUMMON_HOSTILE = 8;
	public static boolean ENABLE_MOVE_BLOCK_BLACKLIST = true;
	
	// Meteor
	private static final String _CATEGORY_METEOR = "Meteor";
	public static float BASIC_METEOR_SIZE = 5.0f;
	public static int BASIC_METEOR_EXPLOSION_POWER = 25;
	public static float BASIC_METEOR_EXPLOSION_DROP_RATE = 0.1f;
	
	// Hostile Mobs
	private static final String _CATEGORY_HOSTILE_MOBS = "Hostile Mobs";
	public static int PERCENT_FOR_SPAWN_HOSTILE_WITH_ARMOR = 10;
	public static int PERCENT_FOR_SPAWN_HOSTILE_ON_HORSE = 15;
	
	// Evoker Crystal
	private static final String _CATEGORY_EVOKER_CRYSTAL = "Evoker Crystal";
	public static int VEX_NUMBER = 3;
	public static boolean VEX_HAS_LIMITED_LIFE = true;
	
	// Integration
	private static final String _CATEGORY_INTEGRATION = "Integration";
	public static boolean SHOW_ADDITIONAL_TOOLTIPS = true;
	public static boolean SHOW_LAST_DEATH_POINT = true;
	
	private VMConfig()
	{
	}
	
	/**
	 * Run in PreInitialization stage. Checks config file and read from it.
	 */
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
	
	/**
	 * Synchronise config and read from it.
	 */
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
	
	/**
	 * Resynchronise config if something changed in config file.
	 */
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
	
	/**
	 * Read from config file.
	 */
	public static void processConfig()
	{
		// Console
		_config.addCustomCategoryComment(_CATEGORY_CONSOLE, "Options connected with Console.");
		SHOW_CUSTOM_TILE_ENTITY_SAVING = _config.getBoolean(
				"showCustomTileEntitySaving", 
				_CATEGORY_CONSOLE, 
				SHOW_CUSTOM_TILE_ENTITY_SAVING, 
				"Should console show when World save / load CustomTileEntity (console spam).");
		
		// Player
		_config.addCustomCategoryComment(_CATEGORY_PLAYER, "Options connected with Player");
		GIVE_PLAYER_CUSTOM_BOOKS_ON_LOGIN = _config.getBoolean(
				"GIVE_PLAYER_CUSTOM_BOOKS_ON_LOGIN", 
				_CATEGORY_PLAYER, 
				GIVE_PLAYER_CUSTOM_BOOKS_ON_LOGIN, 
				"Should Player get Vanilla Magic custom books on spawn (logged in).");
		
		// Machine
		_config.addCustomCategoryComment(_CATEGORY_MACHINE, "Options connected with Vanilla Magic Machines.");
		TILE_SPEEDY_TICKS = _config.getInt(
				"TILE_SPEEDY_TICKS", 
				_CATEGORY_MACHINE, 
				TILE_SPEEDY_TICKS, 
				1, 
				Integer.MAX_VALUE, 
				"The number of ticks in 1 Minecraft tick that Speedy can do to a single block.");
		TILE_SPEEDY_SIZE = _config.getInt(
				"TILE_SPEEDY_SIZE", 
				_CATEGORY_MACHINE, 
				TILE_SPEEDY_SIZE, 
				1, 
				Integer.MAX_VALUE, 
				"Size of the Speedy - Area on which Speedy can operate.");
		TILE_MACHINE_ONE_OPERATION_COST = _config.getInt(
				"TILE_MACHINE_ONE_OPERATION_COST", 
				_CATEGORY_MACHINE, 
				TILE_MACHINE_ONE_OPERATION_COST, 
				1, 
				Integer.MAX_VALUE, 
				"Cost of a single Machine operation.");
		TILE_MACHINE_MAX_TICKS = _config.getInt(
				"TILE_MACHINE_MAX_TICKS", 
				_CATEGORY_MACHINE, 
				TILE_MACHINE_MAX_TICKS, 
				TILE_MACHINE_MAX_TICKS, 
				Integer.MAX_VALUE, 
				"Max ticks (internal fuel) that Machine can store.");
		
		// Item
		_config.addCustomCategoryComment(_CATEGORY_ITEM, "Options connected with Vanilla Magic Items.");
		ACCELERATION_CRYSTAL_UPDATE_TICKS = _config.getInt(
				"ACCELERATION_CRYSTAL_UPDATE_TICKS", 
				_CATEGORY_ITEM, 
				ACCELERATION_CRYSTAL_UPDATE_TICKS, 
				1, 
				Integer.MAX_VALUE, 
				"The number of ticks that should be ticked when Player holds Acceleration Crystal.");
		LIQUID_SUPPRESSION_CRYSTAL_RADIUS = _config.getInt(
				"LIQUID_SUPPRESSION_CRYSTAL_RADIUS", 
				_CATEGORY_ITEM, 
				LIQUID_SUPPRESSION_CRYSTAL_RADIUS, 
				1, 
				Integer.MAX_VALUE, 
				"Radius on which Liquid Suppression Crystal works (in blocks).");
		MOTHER_NATURE_CRYSTAL_RANGE = _config.getInt(
				"MOTHER_NATURE_CRYSTAL_RANGE", 
				_CATEGORY_ITEM, 
				MOTHER_NATURE_CRYSTAL_RANGE, 
				1, 
				Integer.MAX_VALUE, 
				"Range on which Mother Nature Crystal will work.");
		ITEM_MAGNET_RANGE = _config.getInt(
				"ITEM_MAGNET_RANGE", 
				_CATEGORY_ITEM, 
				ITEM_MAGNET_RANGE, 
				1, 
				Integer.MAX_VALUE, 
				"Range on which Item Magnet will work.");
		ITEM_MAGNET_PULLED_ITEMS = _config.getInt(
				"ITEM_MAGNET_PULLED_ITEMS", 
				_CATEGORY_ITEM, 
				ITEM_MAGNET_PULLED_ITEMS, 
				1, 
				Integer.MAX_VALUE, 
				"The maximum number of items that Item Magnet can pull at once.");
		ITEM_CAN_AUTOPLANT = _config.getBoolean(
				"ITEM_CAN_AUTOPLANT", 
				_CATEGORY_ITEM, 
				ITEM_CAN_AUTOPLANT, 
				"If autoplanting items like Sapling, etc should be enabled.");
		
		// Spell
		_config.addCustomCategoryComment(_CATEGORY_SPELL, "Options connected with Vanilla Magic Spells.");
		SPELL_COST_SUMMON_FRIENDLY = _config.getInt(
				"SPELL_COST_SUMMON_FRIENDLY", 
				_CATEGORY_SPELL, 
				SPELL_COST_SUMMON_FRIENDLY, 
				1, 
				64, 
				"Cost of one friendly mob spawn (in items from hand).");
		SPELL_COST_SUMMON_HOSTILE = _config.getInt(
				"SPELL_COST_SUMMON_HOSTILE", 
				_CATEGORY_SPELL, 
				SPELL_COST_SUMMON_HOSTILE, 
				1, 
				64, 
				"Cost of one hostile mob spawn (in items from hand).");
		ENABLE_MOVE_BLOCK_BLACKLIST = _config.getBoolean(
				"ENABLE_MOVE_BLOCK_BLACKLIST", 
				_CATEGORY_SPELL, 
				ENABLE_MOVE_BLOCK_BLACKLIST, 
				"Should Blacklist for Quest Move Block By Book be enabled.");
		
		// Meteor
		_config.addCustomCategoryComment(_CATEGORY_METEOR, "Options connected with Meteor Explosion.");
		BASIC_METEOR_SIZE = _config.getFloat(
				"BASIC_METEOR_SIZE", 
				_CATEGORY_METEOR, 
				BASIC_METEOR_SIZE, 
				1.0f, 
				Float.MAX_VALUE, 
				"Basic size of the Meteor.");
		BASIC_METEOR_EXPLOSION_POWER = _config.getInt(
				"BASIC_METEOR_EXPLOSION_POWER", 
				_CATEGORY_METEOR, 
				BASIC_METEOR_EXPLOSION_POWER, 
				1, 
				Integer.MAX_VALUE, 
				"Meteor explosion power.");
		BASIC_METEOR_EXPLOSION_DROP_RATE = _config.getFloat(
				"BASIC_METEOR_EXPLOSION_DROP_RATE", 
				_CATEGORY_METEOR, 
				BASIC_METEOR_EXPLOSION_DROP_RATE, 
				0.0f, 
				1.0f, 
				"Rate between 0-1 for block drops from explosion.");
		
		// Hostile Mobs
		_config.addCustomCategoryComment(_CATEGORY_HOSTILE_MOBS, "Options connected with Hostile Mobs Spawning with Spell.");
		PERCENT_FOR_SPAWN_HOSTILE_WITH_ARMOR = _config.getInt(
				"PERCENT_FOR_SPAWN_HOSTILE_WITH_ARMOR", 
				_CATEGORY_HOSTILE_MOBS, 
				PERCENT_FOR_SPAWN_HOSTILE_WITH_ARMOR, 
				1, 
				100, 
				"Percent with which there is a chance for spawning a Mob with Armor (if possible for Armor).");
		PERCENT_FOR_SPAWN_HOSTILE_ON_HORSE = _config.getInt(
				"PERCENT_FOR_SPAWN_HOSTILE_ON_HORSE", 
				_CATEGORY_HOSTILE_MOBS, 
				PERCENT_FOR_SPAWN_HOSTILE_ON_HORSE, 
				1, 
				100, 
				"Percent with which there is a chance for spawning a Mob on Horse (if Mob has equal Horse).");
		
		// Evoker Crystal
		_config.addCustomCategoryComment(_CATEGORY_EVOKER_CRYSTAL, "Options connected with Evoker Crystal.");
		VEX_NUMBER = _config.getInt(
				"VEX_NUMBER",
				_CATEGORY_EVOKER_CRYSTAL,
				VEX_NUMBER,
				1,
				Integer.MAX_VALUE,
				"The number of Vex's that can be spawn using Evoker Crystal."
				);
		VEX_HAS_LIMITED_LIFE = _config.getBoolean(
				"VEX_HAS_LIMITED_LIFE", 
				_CATEGORY_EVOKER_CRYSTAL, 
				VEX_HAS_LIMITED_LIFE, 
				"Should spawned Vex have limited life.");
		
		// Integration
		_config.addCustomCategoryComment(_CATEGORY_INTEGRATION, "Options connected with Integrations with VM");
		SHOW_ADDITIONAL_TOOLTIPS = _config.getBoolean(
				"SHOW_ADDITIONAL_TOOLTIPS", 
				_CATEGORY_INTEGRATION, 
				SHOW_ADDITIONAL_TOOLTIPS, 
				"Additional tooltips (weapons durability, food saturation, etc.)");
		SHOW_LAST_DEATH_POINT = _config.getBoolean(
				"SHOW_LAST_DEATH_POINT", 
				_CATEGORY_INTEGRATION, 
				SHOW_ADDITIONAL_TOOLTIPS, 
				"Death info (last death position)");
		
		_config.save();
	}
	
	/**
	 * Run in Initialization stage.
	 */
	public static void init() 
	{
	}
	
	/**
	 * Run in PostInitialization stage.
	 */
	public static void postInit() 
	{
	}
}