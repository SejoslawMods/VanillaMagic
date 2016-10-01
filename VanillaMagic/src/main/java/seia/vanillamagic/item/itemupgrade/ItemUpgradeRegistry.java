package seia.vanillamagic.item.itemupgrade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import seia.vanillamagic.VanillaMagic;
import seia.vanillamagic.item.itemupgrade.upgrade.IItemUpgrade;
import seia.vanillamagic.item.itemupgrade.upgrade.UpgradeAutosmelt;

/**
 * This is the base registry which will hold ALL the upgrades for different items.
 */
public class ItemUpgradeRegistry
{
	public static final ItemUpgradeRegistry INSTANCE = new ItemUpgradeRegistry();
	
	final class ItemEntry
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
	
	private final Map<String, List<ItemEntry>> MAPPING_ITEMNAME_ITEMENTRY = new HashMap<String, List<ItemEntry>>();
	private final Map<String, List<IItemUpgrade>> MAPPING_ITEMNAME_UPGRADE = new HashMap<String, List<IItemUpgrade>>();
	private final List<Item> ITEMS = ForgeRegistries.ITEMS.getValues();
	
	private ItemUpgradeRegistry()
	{
		addItemMapping("_pickaxe");
		addItemMapping("_axe");
		addItemMapping("_sword");
		addItemMapping("_hoe");
		addItemMapping("_shovel");
		
		addUpgradeMapping("_pickaxe", UpgradeAutosmelt.class);
	}
	
	/**
	 * Main method for registering the upgrades.
	 * 
	 * @param itemMappingName -> For instance: "_pickaxe" or "_sword" or "_axe" or "_myNewMapping" itd.
	 * @param clazz -> MyUpgradeClass.class
	 */
	public void addUpgradeMapping(String mappingName, Class<? extends IItemUpgrade> clazz)
	{
		try
		{
			if(MAPPING_ITEMNAME_UPGRADE.containsKey(mappingName))
			{
				MAPPING_ITEMNAME_UPGRADE.get(mappingName).add(clazz.newInstance());
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
	 * Add a single value to the mapping. If mapping doesn't exists it will add a new mapping. <br>
	 * For instance: addItemToMapping("_pickaxe", new MyItemPickaxe()); <br>
	 * or <br>
	 * addItemToMapping("_myMapping", new MyItem());
	 */
	public void addItemToMapping(String mappingName, Item item)
	{
		if(MAPPING_ITEMNAME_ITEMENTRY.containsKey(mappingName))
		{
			MAPPING_ITEMNAME_ITEMENTRY.get(mappingName).add(new ItemEntry(mappingName, item));
		}
		else
		{
			MAPPING_ITEMNAME_ITEMENTRY.put(mappingName, new ArrayList<ItemEntry>());
			VanillaMagic.LOGGER.log(Level.ERROR, "Created mapping for key: " + mappingName);
			MAPPING_ITEMNAME_ITEMENTRY.get(mappingName).add(new ItemEntry(mappingName, item));
		}
		// Add upgrade map only if it doesn't exists for the given mappingName
		if(!MAPPING_ITEMNAME_UPGRADE.containsKey(mappingName))
		{
			MAPPING_ITEMNAME_UPGRADE.put(mappingName, new ArrayList<IItemUpgrade>());
		}
	}
	
	/**
	 * Add a new mapping. <br>
	 * In VanillaMagic mapping will start with "_" so "pickaxe" will be -> "_pickaxe". <br>
	 * It was made this was to prevent "pickaxe" and "axe" being counted as one mapping.
	 */
	public void addItemMapping(String mappingName)
	{
		// we want to register mapping only once for given key
		if(MAPPING_ITEMNAME_ITEMENTRY.containsKey(mappingName))
		{
			return;
		}
		MAPPING_ITEMNAME_ITEMENTRY.put(mappingName, new ArrayList<ItemEntry>());
		VanillaMagic.LOGGER.log(Level.ERROR, "Created mapping for key: " + mappingName);
		for(Item item : ITEMS)
		{
			if(item.getUnlocalizedName().contains(mappingName))
			{
				MAPPING_ITEMNAME_ITEMENTRY.get(mappingName).add(new ItemEntry(mappingName, item));
				// register mapping for given key
				if(!MAPPING_ITEMNAME_UPGRADE.containsKey(mappingName))
				{
					MAPPING_ITEMNAME_UPGRADE.put(mappingName, new ArrayList<IItemUpgrade>());
				}
			}
		}
	}
	
	/**
	 * @param base -> Basic item. For instance: pickaxe
	 * @param ingredient -> Item needed to upgrade.
	 * @return ItemStack with upgrade and written NBT data.
	 */
	@Nullable
	public ItemStack getResult(ItemStack base, ItemStack ingredient)
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
				if(upgrade.getIngredient().stackSize == ingredient.stackSize)
				{
					return upgrade.getResult(base);
				}
			}
		}
		return null;
	}

	@Nullable
	public String getMappingNameFromItemStack(ItemStack stack) 
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
		return null;
	}
}