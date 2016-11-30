package seia.vanillamagic.itemupgrade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import seia.vanillamagic.api.item.itemupgrade.IItemUpgrade;
import seia.vanillamagic.api.item.itemupgrade.ItemUpgradeAPI;
import seia.vanillamagic.core.VanillaMagic;
import seia.vanillamagic.itemupgrade.upgrade.UpgradeAutosmelt;
import seia.vanillamagic.util.ItemStackHelper;

/**
 * This is the base registry which will hold ALL the upgrades for different items.
 */
public class ItemUpgradeRegistry
{
	static final class ItemEntry
	{
		public final String mappingName;
		public final Item item;
		public final ItemStack stack;
		
		public ItemEntry(String mappingName, Item item)
		{
			this.mappingName = mappingName;
			this.item = item;
			this.stack = new ItemStack(this.item);
		}
	}
	
	/**
	 * Mapping itemName to list of available items with the same name.<br>
	 * For instance: "_pickaxe" -> all pickaxes
	 */
	private static final Map<String, List<ItemEntry>> MAPPING_ITEMNAME_ITEMENTRY = new HashMap<String, List<ItemEntry>>();
	/**
	 * Mapping itemName to list of IItemUpgrades for that mapping.<br>
	 * For instance: "_pickaxe" -> all upgrades for pickaxes
	 */
	private static final Map<String, List<IItemUpgrade>> MAPPING_ITEMNAME_UPGRADE = new HashMap<String, List<IItemUpgrade>>();
	/**
	 * Registered upgrades with events.
	 */
	private static final List<IItemUpgrade> EVENT_BUS_REGISTERED_UPGRADES = new ArrayList<IItemUpgrade>();
	/**
	 * All items to which we can add upgrade.<br>
	 * For instance: all pickaxes, all axes, etc.
	 */
	private static final List<ItemEntry> BASE_ITEMS = new ArrayList<ItemEntry>();
	private static final List<Item> ITEMS = ForgeRegistries.ITEMS.getValues();
	
	private ItemUpgradeRegistry()
	{
	}
	
	static 
	{
		addItemMapping("_pickaxe");
		addItemMapping("_axe");
		addItemMapping("_sword");
		addItemMapping("_hoe");
		addItemMapping("_shovel");
		
		addUpgradeMapping("_pickaxe", UpgradeAutosmelt.class);
	}
	
	/**
	 * This method is just to start the static.
	 */
	public static void start()
	{
		VanillaMagic.LOGGER.log(Level.INFO, "ItemUpgradeRegistry created");
	}
	
	/**
	 * @see ItemUpgradeAPI#addUpgradeMapping(String, Class)
	 */
	public static void addUpgradeMapping(String mappingName, Class<? extends IItemUpgrade> clazz)
	{
		try
		{
			if(MAPPING_ITEMNAME_UPGRADE.containsKey(mappingName))
			{
				MAPPING_ITEMNAME_UPGRADE.get(mappingName).add(clazz.newInstance());
				VanillaMagic.LOGGER.log(Level.INFO, "Added upgrade: " + clazz.getSimpleName() + ", for key: " + mappingName);
			}
			else
			{
				VanillaMagic.LOGGER.log(Level.ERROR, "Couldn't find key for mapping: " + mappingName + ", for class: " + clazz.getSimpleName());
			}
		}
		catch(Exception e)
		{
			VanillaMagic.LOGGER.log(Level.ERROR, "Error while creating the instance of upgrade class: " + clazz.getSimpleName());
			VanillaMagic.LOGGER.log(Level.ERROR, "Didn't register upgrade from class: " + clazz.getSimpleName());
			e.printStackTrace();
		}
	}
	
	/**
	 * @see ItemUpgradeAPI#addItemToMapping(String, Item)
	 */
	public static void addItemToMapping(String mappingName, Item item)
	{
		ItemEntry itemEntry = new ItemEntry(mappingName, item);
		if(MAPPING_ITEMNAME_ITEMENTRY.containsKey(mappingName))
		{
			MAPPING_ITEMNAME_ITEMENTRY.get(mappingName).add(itemEntry);
			BASE_ITEMS.add(itemEntry);
		}
		else
		{
			MAPPING_ITEMNAME_ITEMENTRY.put(mappingName, new ArrayList<ItemEntry>());
			VanillaMagic.LOGGER.log(Level.INFO, "Created mapping for key: " + mappingName);
			MAPPING_ITEMNAME_ITEMENTRY.get(mappingName).add(itemEntry);
			BASE_ITEMS.add(itemEntry);
		}
		// Add upgrade map only if it doesn't exists for the given mappingName
		if(!MAPPING_ITEMNAME_UPGRADE.containsKey(mappingName))
		{
			MAPPING_ITEMNAME_UPGRADE.put(mappingName, new ArrayList<IItemUpgrade>());
			VanillaMagic.LOGGER.log(Level.INFO, "Created mapping for upgrades for key: " + mappingName);
		}
	}
	
	/**
	 * @see ItemUpgradeAPI#addItemMapping(String)
	 */
	public static void addItemMapping(String mappingName)
	{
		// we want to register mapping only once for given key
		if(MAPPING_ITEMNAME_ITEMENTRY.containsKey(mappingName))
		{
			return;
		}
		MAPPING_ITEMNAME_ITEMENTRY.put(mappingName, new ArrayList<ItemEntry>());
		MAPPING_ITEMNAME_UPGRADE.put(mappingName, new ArrayList<IItemUpgrade>());
		VanillaMagic.LOGGER.log(Level.INFO, "Created mapping for key: " + mappingName);
		int registeredItems = 0;
		for(Item item : ITEMS)
		{
			if(item.getRegistryName().getResourcePath().contains(mappingName))
			{
				ItemEntry itemEntry = new ItemEntry(mappingName, item);
				MAPPING_ITEMNAME_ITEMENTRY.get(mappingName).add(itemEntry);
				BASE_ITEMS.add(itemEntry);
				registeredItems++;
			}
		}
		VanillaMagic.LOGGER.log(Level.INFO, "Registered items: " + registeredItems + " for key: " + mappingName);
	}
	
	/**
	 * @see ItemUpgradeAPI#getResult(ItemStack, ItemStack)
	 */
	@Nullable
	public static ItemStack getResult(ItemStack base, ItemStack ingredient)
	{
		String mappingName = getMappingNameFromItemStack(base);
		if(mappingName == null)
		{
			return null;
		}
		List<IItemUpgrade> upgrades = MAPPING_ITEMNAME_UPGRADE.get(mappingName);
		for(IItemUpgrade upgrade : upgrades)
		{
			if(upgrade.getIngredient().getItem() == ingredient.getItem())
			{
				//if(upgrade.getIngredient().stackSize == ingredient.stackSize)
				if(ItemStackHelper.getStackSize(upgrade.getIngredient()) == 
						ItemStackHelper.getStackSize(ingredient))
				{
					return upgrade.getResult(base);
				}
			}
		}
		return null;
	}
	
	public static String getMappingNameFromItemStack(ItemStack stack) 
	{
		for(Entry<String, List<ItemEntry>> mappingEntry : MAPPING_ITEMNAME_ITEMENTRY.entrySet())
		{
			for(ItemEntry itemEntry : mappingEntry.getValue())
			{
				if(itemEntry.item == stack.getItem())
				{
					return itemEntry.mappingName;
				}
			}
		}
		return "";
	}
	
	/**
	 * Returns the list of all the currently registered "base" items. <br>
	 * For instance: swords, axes, pickaxes, etc.
	 */
	public static List<ItemEntry> getBaseItems()
	{
		return BASE_ITEMS;
	}
	
	/**
	 * Returns the map of category names for upgrades. <br>
	 * For instance (mapping): "_pickaxe" -> the list of all available pickaxes upgrades.
	 */
	public static Map<String, List<IItemUpgrade>> getUpgradesMap()
	{
		return MAPPING_ITEMNAME_UPGRADE;
	}
	
	public static void registerEvents()
	{
		int registered = 0;
		for(Entry<String, List<IItemUpgrade>> itemCategory : MAPPING_ITEMNAME_UPGRADE.entrySet())
		{
			for(IItemUpgrade upgrade : itemCategory.getValue())
			{
				MinecraftForge.EVENT_BUS.register(upgrade);
				EVENT_BUS_REGISTERED_UPGRADES.add(upgrade);
				registered++;
			}
		}
		VanillaMagic.LOGGER.log(Level.INFO, "Registered Upgrade Events: " + registered);
	}
}