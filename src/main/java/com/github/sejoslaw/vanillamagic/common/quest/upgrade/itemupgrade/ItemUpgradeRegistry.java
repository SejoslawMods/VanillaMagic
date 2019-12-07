package com.github.sejoslaw.vanillamagic.quest.upgrade.itemupgrade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import com.github.sejoslaw.vanillamagic.api.upgrade.itemupgrade.IItemUpgrade;
import com.github.sejoslaw.vanillamagic.api.upgrade.itemupgrade.ItemUpgradeAPI;
import com.github.sejoslaw.vanillamagic.core.VanillaMagic;
import com.github.sejoslaw.vanillamagic.quest.upgrade.itemupgrade.upgrades.UpgradeAutosmelt;
import com.github.sejoslaw.vanillamagic.quest.upgrade.itemupgrade.upgrades.UpgradeLifesteal;
import com.github.sejoslaw.vanillamagic.quest.upgrade.itemupgrade.upgrades.UpgradeThor;
import com.github.sejoslaw.vanillamagic.quest.upgrade.itemupgrade.upgrades.UpgradeWitherEffect;
import com.github.sejoslaw.vanillamagic.util.EventUtil;
import com.github.sejoslaw.vanillamagic.util.ItemStackUtil;

/**
 * This is the base registry which will hold ALL the upgrades for dif ferent
 * items.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class ItemUpgradeRegistry {
	static final class ItemEntry {
		public final String mappingName;
		public final Item item;
		public final ItemStack stack;
		public final String localizedName; // for instance -> "Pickaxe"

		public ItemEntry(String mappingName, Item item, String localizedName) {
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
	private static final Map<String, List<ItemEntry>> MAPPING_ITEMNAME_ITEMENTRY = new HashMap<>();
	/**
	 * Mapping itemName to list of IItemUpgrades for that mapping.<br>
	 * For instance: "_pickaxe" -> all upgrades for pickaxes
	 */
	private static final Map<String, List<IItemUpgrade>> MAPPING_ITEMNAME_UPGRADE = new HashMap<>();
	/**
	 * Mapping itemName to it's localizedName.<br>
	 * For instance: "_pickaxe" -> "Pickaxe"
	 */
	private static final Map<String, String> MAPPING_ITEMNAME_LOCALIZEDNAME = new HashMap<>();
	/**
	 * Registered upgrades with events.
	 */
	private static final List<IItemUpgrade> EVENT_BUS_REGISTERED_UPGRADES = new ArrayList<>();
	/**
	 * List of all items to which we can add upgrades.
	 */
	private static final List<ItemEntry> BASE_ITEMS = new ArrayList<ItemEntry>();
	/**
	 * Used for registering vanilla items.
	 */
	private static final Collection<Item> ITEMS = ForgeRegistries.ITEMS.getValues();

	private ItemUpgradeRegistry() {
	}

	/**
	 * This method is just to start the static.
	 */
	public static void preInit() {
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
	 * @see ItemUpgradeAPI#addUpgradeMapping(String, Class)
	 */
	public static void addUpgradeMapping(String mappingName, Class<? extends IItemUpgrade> clazz,
			String localizedName) {
		try {
			if (MAPPING_ITEMNAME_UPGRADE.containsKey(mappingName)) {
				MAPPING_ITEMNAME_UPGRADE.get(mappingName).add(clazz.newInstance());
				VanillaMagic.logInfo("Added upgrade: " + clazz.getSimpleName() + ", for key: " + mappingName);
			} else {
				VanillaMagic.logInfo(
						"Couldn't find key for mapping: " + mappingName + ", for class: " + clazz.getSimpleName());
				VanillaMagic.logInfo("Creating new mapping for key: " + mappingName);
				MAPPING_ITEMNAME_ITEMENTRY.put(mappingName, new ArrayList<>());
				List<IItemUpgrade> upgrades = new ArrayList<>();
				IItemUpgrade upgrade = clazz.newInstance();
				upgrades.add(upgrade);
				MAPPING_ITEMNAME_UPGRADE.put(mappingName, upgrades);
				MAPPING_ITEMNAME_LOCALIZEDNAME.put(mappingName, localizedName);
				EVENT_BUS_REGISTERED_UPGRADES.add(upgrade);
			}
		} catch (Exception e) {
			VanillaMagic.log(Level.ERROR,
					"Error while creating the instance of upgrade class: " + clazz.getSimpleName());
			VanillaMagic.log(Level.ERROR, "Didn't register upgrade from class: " + clazz.getSimpleName());
			e.printStackTrace();
		}
	}

	/**
	 * @see ItemUpgradeAPI#addItemToMapping(String, Item)
	 */
	public static void addItemToMapping(String mappingName, Item item, String localizedName) {
		ItemEntry itemEntry = new ItemEntry(mappingName, item, localizedName);

		if (MAPPING_ITEMNAME_ITEMENTRY.containsKey(mappingName)) {
			MAPPING_ITEMNAME_ITEMENTRY.get(mappingName).add(itemEntry);
		} else {
			MAPPING_ITEMNAME_ITEMENTRY.put(mappingName, new ArrayList<ItemEntry>());
			VanillaMagic.logInfo("Created mapping for key: " + mappingName);
			MAPPING_ITEMNAME_ITEMENTRY.get(mappingName).add(itemEntry);
			BASE_ITEMS.add(itemEntry);
		}

		// Add upgrade map only if it doesn't exists for the given mappingName
		if (!MAPPING_ITEMNAME_UPGRADE.containsKey(mappingName)) {
			MAPPING_ITEMNAME_UPGRADE.put(mappingName, new ArrayList<IItemUpgrade>());
			VanillaMagic.logInfo("Created mapping for upgrades for key: " + mappingName);
		}
	}

	/**
	 * @see ItemUpgradeAPI#addItemMapping(String)
	 */
	public static void addItemMapping(String mappingName, String localizedName) {
		if (MAPPING_ITEMNAME_ITEMENTRY.containsKey(mappingName)) {
			return;
		}

		MAPPING_ITEMNAME_ITEMENTRY.put(mappingName, new ArrayList<ItemEntry>());
		MAPPING_ITEMNAME_UPGRADE.put(mappingName, new ArrayList<IItemUpgrade>());
		MAPPING_ITEMNAME_LOCALIZEDNAME.put(mappingName, localizedName);
		VanillaMagic.logInfo("Created mapping for key: " + mappingName + ", with name: " + localizedName);
		int registeredItems = 0;

		for (Item item : ITEMS) {
			if (item.getRegistryName().getResourcePath().contains(mappingName)) {
				ItemEntry itemEntry = new ItemEntry(mappingName, item, localizedName);
				MAPPING_ITEMNAME_ITEMENTRY.get(mappingName).add(itemEntry);
				BASE_ITEMS.add(new ItemEntry(mappingName, item, item.getUnlocalizedName()));
				registeredItems++;
			}
		}
		VanillaMagic.logInfo("Registered items: " + registeredItems + " for key: " + mappingName);
	}

	/**
	 * @see ItemUpgradeAPI#getResult(ItemStack, ItemStack)
	 */
	@Nullable
	public static ItemStack getResult(ItemStack base, ItemStack ingredient) {
		String mappingName = getMappingNameFromItemStack(base);

		if (mappingName == null) {
			return null;
		}

		List<IItemUpgrade> upgrades = MAPPING_ITEMNAME_UPGRADE.get(mappingName);

		for (IItemUpgrade upgrade : upgrades) {
			if ((upgrade.getIngredient().getItem() == ingredient.getItem())
					&& (upgrade.getIngredient().getMetadata() == ingredient.getMetadata()) && (ItemStackUtil
							.getStackSize(upgrade.getIngredient()) == ItemStackUtil.getStackSize(ingredient))) {
				return upgrade.getResult(base);
			}
		}
		return null;
	}

	/**
	 * @return Returns name of the mapping from given ItemStack.
	 */
	public static String getMappingNameFromItemStack(ItemStack stack) {
		for (Entry<String, List<ItemEntry>> mappingEntry : MAPPING_ITEMNAME_ITEMENTRY.entrySet()) {
			for (ItemEntry itemEntry : mappingEntry.getValue()) {
				if (itemEntry.item == stack.getItem()) {
					return itemEntry.mappingName;
				}
			}
		}

		return "";
	}

	/**
	 * @return Returns the map of category names for upgrades. <br>
	 *         For instance (mapping): "_pickaxe" -> the list of all available
	 *         pickaxes upgrades.
	 */
	public static Map<String, List<IItemUpgrade>> getUpgradesMap() {
		return MAPPING_ITEMNAME_UPGRADE;
	}

	/**
	 * @return Returns the localized name from given mapping.
	 */
	public static String getLocalizedNameForMapping(String mappingName) {
		return MAPPING_ITEMNAME_LOCALIZEDNAME.get(mappingName);
	}

	/**
	 * Register any additional Event inside CustomItem class.
	 */
	public static void registerEvents() {
		int registered = 0;

		for (Entry<String, List<IItemUpgrade>> itemCategory : MAPPING_ITEMNAME_UPGRADE.entrySet()) {
			for (IItemUpgrade upgrade : itemCategory.getValue()) {
				EventUtil.registerEvent(upgrade);
				EVENT_BUS_REGISTERED_UPGRADES.add(upgrade);
				registered++;
			}
		}

		VanillaMagic.logInfo("Registered Upgrade Events: " + registered);
	}

	/**
	 * @return Returns base items list.
	 */
	public static List<ItemEntry> getBaseItems() {
		return BASE_ITEMS;
	}

	/**
	 * @return Returns the given list filled with all possible outcome items with
	 *         all possible upgrades.
	 */
	public static NonNullList<ItemStack> fillList(NonNullList<ItemStack> list) {
		for (Entry<String, List<IItemUpgrade>> upgrade : MAPPING_ITEMNAME_UPGRADE.entrySet()) {
			String itemName = upgrade.getKey();
			List<IItemUpgrade> upgrades = upgrade.getValue();
			List<ItemEntry> entries = MAPPING_ITEMNAME_ITEMENTRY.get(itemName);

			for (IItemUpgrade itemUpgrade : upgrades) {
				for (ItemEntry itemEntry : entries) {
					list.add(itemUpgrade.getResult(itemEntry.stack));
				}
			}
		}
		return list;
	}
}