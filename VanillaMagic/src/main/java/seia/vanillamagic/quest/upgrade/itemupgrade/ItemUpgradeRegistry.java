package seia.vanillamagic.quest.upgrade.itemupgrade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import seia.vanillamagic.api.upgrade.itemupgrade.IItemUpgrade;
import seia.vanillamagic.api.upgrade.itemupgrade.ItemUpgradeAPI;
import seia.vanillamagic.core.VanillaMagic;
import seia.vanillamagic.quest.upgrade.itemupgrade.upgrades.UpgradeAutosmelt;
import seia.vanillamagic.quest.upgrade.itemupgrade.upgrades.UpgradeLifesteal;
import seia.vanillamagic.quest.upgrade.itemupgrade.upgrades.UpgradeThor;
import seia.vanillamagic.quest.upgrade.itemupgrade.upgrades.UpgradeWitherEffect;
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
		public final String localizedName; // for instance -> "Pickaxe"
		
		public ItemEntry(String mappingName, Item item, String localizedName)
		{
			this.mappingName = mappingName;
			this.item = item;
			this.stack = new ItemStack(this.item);
			this.localizedName = localizedName;
		}
	}
	
	/**
	 * Mapping itemName to list of available items with the same name.<br>
	 * For instance: "_pickaxe" -> all pickaxes
	 */
	private static final Map<String, List<ItemEntry>> _MAPPING_ITEMNAME_ITEMENTRY = new HashMap<>();
	/**
	 * Mapping itemName to list of IItemUpgrades for that mapping.<br>
	 * For instance: "_pickaxe" -> all upgrades for pickaxes
	 */
	private static final Map<String, List<IItemUpgrade>> _MAPPING_ITEMNAME_UPGRADE = new HashMap<>();
	/**
	 * Mapping itemName to it's localizedName.<br>
	 * For instance: "_pickaxe" -> "Pickaxe"
	 */
	private static final Map<String, String> _MAPPING_ITEMNAME_LOCALIZEDNAME = new HashMap<>();
	/**
	 * Registered upgrades with events.
	 */
	private static final List<IItemUpgrade> _EVENT_BUS_REGISTERED_UPGRADES = new ArrayList<>();
	/**
	 * List of all items to which we can add upgrades.
	 */
	private static final List<ItemEntry> _BASE_ITEMS = new ArrayList<ItemEntry>();
	/**
	 * Used for registering vanilla items.
	 */
	private static final List<Item> _ITEMS = ForgeRegistries.ITEMS.getValues();
	
	private ItemUpgradeRegistry()
	{
	}
	
	static 
	{
		// Mappings
		addItemMapping("_pickaxe", "Pickaxe");
		addItemMapping("_axe", "Axe");
		addItemMapping("_sword", "Sword");
		addItemMapping("_hoe", "Hoe");
		addItemMapping("_shovel", "Shovel");
		
		// Upgrades
		// Pickaxe Upgrades
		addUpgradeMapping("_pickaxe", UpgradeAutosmelt.class, "Pickaxe");
		// Sword Upgrades
		addUpgradeMapping("_sword", UpgradeWitherEffect.class, "Sword");
		addUpgradeMapping("_sword", UpgradeLifesteal.class, "Sword");
		addUpgradeMapping("_sword", UpgradeThor.class, "Sword");
	}
	
	/**
	 * This method is just to start the static.
	 */
	public static void preInit()
	{
		VanillaMagic.LOGGER.log(Level.INFO, "ItemUpgradeRegistry created");
	}
	
	/**
	 * @see ItemUpgradeAPI#addUpgradeMapping(String, Class)
	 */
	public static void addUpgradeMapping(String mappingName, Class<? extends IItemUpgrade> clazz, String localizedName)
	{
		try
		{
			if(_MAPPING_ITEMNAME_UPGRADE.containsKey(mappingName))
			{
				_MAPPING_ITEMNAME_UPGRADE.get(mappingName).add(clazz.newInstance());
				VanillaMagic.LOGGER.log(Level.INFO, "Added upgrade: " + clazz.getSimpleName() + ", for key: " + mappingName);
			}
			else
			{
				VanillaMagic.LOGGER.log(Level.ERROR, "Couldn't find key for mapping: " + mappingName + ", for class: " + clazz.getSimpleName());
				VanillaMagic.LOGGER.log(Level.INFO, "Creating new mapping for key: " + mappingName);
				_MAPPING_ITEMNAME_ITEMENTRY.put(mappingName, new ArrayList<>());
				List<IItemUpgrade> upgrades = new ArrayList<>();
				IItemUpgrade upgrade = clazz.newInstance();
				upgrades.add(upgrade);
				_MAPPING_ITEMNAME_UPGRADE.put(mappingName, upgrades);
				_MAPPING_ITEMNAME_LOCALIZEDNAME.put(mappingName, localizedName);
				_EVENT_BUS_REGISTERED_UPGRADES.add(upgrade);
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
	public static void addItemToMapping(String mappingName, Item item, String localizedName)
	{
		ItemEntry itemEntry = new ItemEntry(mappingName, item, localizedName);
		if(_MAPPING_ITEMNAME_ITEMENTRY.containsKey(mappingName))
		{
			_MAPPING_ITEMNAME_ITEMENTRY.get(mappingName).add(itemEntry);
		}
		else
		{
			_MAPPING_ITEMNAME_ITEMENTRY.put(mappingName, new ArrayList<ItemEntry>());
			VanillaMagic.LOGGER.log(Level.INFO, "Created mapping for key: " + mappingName);
			_MAPPING_ITEMNAME_ITEMENTRY.get(mappingName).add(itemEntry);
			_BASE_ITEMS.add(itemEntry);
		}
		// Add upgrade map only if it doesn't exists for the given mappingName
		if(!_MAPPING_ITEMNAME_UPGRADE.containsKey(mappingName))
		{
			_MAPPING_ITEMNAME_UPGRADE.put(mappingName, new ArrayList<IItemUpgrade>());
			VanillaMagic.LOGGER.log(Level.INFO, "Created mapping for upgrades for key: " + mappingName);
		}
	}
	
	/**
	 * @see ItemUpgradeAPI#addItemMapping(String)
	 */
	public static void addItemMapping(String mappingName, String localizedName)
	{
		// we want to register mapping only once for given key
		if(_MAPPING_ITEMNAME_ITEMENTRY.containsKey(mappingName))
		{
			return;
		}
		_MAPPING_ITEMNAME_ITEMENTRY.put(mappingName, new ArrayList<ItemEntry>());
		_MAPPING_ITEMNAME_UPGRADE.put(mappingName, new ArrayList<IItemUpgrade>());
		_MAPPING_ITEMNAME_LOCALIZEDNAME.put(mappingName, localizedName);
		VanillaMagic.LOGGER.log(Level.INFO, "Created mapping for key: " + mappingName + ", with name: " + localizedName);
		int registeredItems = 0;
		for(Item item : _ITEMS)
		{
			if(item.getRegistryName().getResourcePath().contains(mappingName))
			{
				ItemEntry itemEntry = new ItemEntry(mappingName, item, localizedName);
				_MAPPING_ITEMNAME_ITEMENTRY.get(mappingName).add(itemEntry);
				_BASE_ITEMS.add(new ItemEntry(mappingName, item, item.getUnlocalizedName()));
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
		List<IItemUpgrade> upgrades = _MAPPING_ITEMNAME_UPGRADE.get(mappingName);
		for(IItemUpgrade upgrade : upgrades)
			if(upgrade.getIngredient().getItem() == ingredient.getItem()) // Check Item
				if(upgrade.getIngredient().getMetadata() == ingredient.getMetadata()) // Check Item Metadata
					if(ItemStackHelper.getStackSize(upgrade.getIngredient()) == ItemStackHelper.getStackSize(ingredient)) // Check StackSize
						return upgrade.getResult(base);
		return null;
	}
	
	/**
	 * @return Returns name of the mapping from given ItemStack.
	 */
	public static String getMappingNameFromItemStack(ItemStack stack) 
	{
		for(Entry<String, List<ItemEntry>> mappingEntry : _MAPPING_ITEMNAME_ITEMENTRY.entrySet())
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
	 * @return Returns the map of category names for upgrades. <br>
	 * For instance (mapping): "_pickaxe" -> the list of all available pickaxes upgrades.
	 */
	public static Map<String, List<IItemUpgrade>> getUpgradesMap()
	{
		return _MAPPING_ITEMNAME_UPGRADE;
	}
	
	/**
	 * @return Returns the localized name from given mapping.
	 */
	public static String getLocalizedNameForMapping(String mappingName)
	{
		return _MAPPING_ITEMNAME_LOCALIZEDNAME.get(mappingName);
	}
	
	/**
	 * Register any additional Event inside CustomItem class.
	 */
	public static void registerEvents()
	{
		int registered = 0;
		for(Entry<String, List<IItemUpgrade>> itemCategory : _MAPPING_ITEMNAME_UPGRADE.entrySet())
		{
			for(IItemUpgrade upgrade : itemCategory.getValue())
			{
				MinecraftForge.EVENT_BUS.register(upgrade);
				_EVENT_BUS_REGISTERED_UPGRADES.add(upgrade);
				registered++;
			}
		}
		VanillaMagic.LOGGER.log(Level.INFO, "Registered Upgrade Events: " + registered);
	}
	
	/**
	 * @return Returns base items list.
	 */
	public static List<ItemEntry> getBaseItems() 
	{
		return _BASE_ITEMS;
	}
	
	/**
	 * @return Returns the given list filled with all possible outcome items with all possible upgrades.
	 */
	public static NonNullList<ItemStack> fillList(NonNullList<ItemStack> list) 
	{
		// All registered upgrades
		for(Entry<String, List<IItemUpgrade>> upgrade : _MAPPING_ITEMNAME_UPGRADE.entrySet())
		{
			// item type - for instance: "_pickaxe"
			String itemName = upgrade.getKey();
			// all upgrades registered for this item type
			List<IItemUpgrade> upgrades = upgrade.getValue();
			// all entries for this upgrade
			List<ItemEntry> entries = _MAPPING_ITEMNAME_ITEMENTRY.get(itemName);
			// all upgrades
			for(IItemUpgrade itemUpgrade : upgrades)
			{
				// all found entries
				for(ItemEntry itemEntry : entries)
				{
					list.add(itemUpgrade.getResult(itemEntry.stack));
				}
			}
		}
		return list;
	}
}