package seia.vanillamagic.api.item.itemupgrade;

import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Instead of this, use {@link ItemUpgradeAPI}
 */
public interface IItemUpgradeRegistry 
{
	/**
	 * Main method for registering the upgrades.
	 * 
	 * @param itemMappingName -> For instance: "_pickaxe" or "_sword" or "_axe" or "_myNewMapping" itd.
	 * @param clazz -> MyUpgradeClass.class
	 */
	void addUpgradeMapping(String mappingName, Class<? extends IItemUpgrade> clazz);
	
	/**
	 * Add a single value to the mapping. If mapping doesn't exists it will add a new mapping. <br>
	 * For instance: addItemToMapping("_pickaxe", new MyItemPickaxe()); <br>
	 * or <br>
	 * addItemToMapping("_myMapping", new MyItem());
	 */
	void addItemToMapping(String mappingName, Item item);
	
	/**
	 * Add a new mapping. <br>
	 * In VanillaMagic mapping will start with "_" so "pickaxe" will be -> "_pickaxe". <br>
	 * It was made this was to prevent "pickaxe" and "axe" being counted as one mapping.
	 */
	void addItemMapping(String mappingName);
	
	/**
	 * @param base -> Basic item. For instance: pickaxe
	 * @param ingredient -> Item needed to upgrade.
	 * @return ItemStack with upgrade and written NBT data.
	 */
	@Nullable
	ItemStack getResult(ItemStack base, ItemStack ingredient);
	
	String getMappingNameFromItemStack(ItemStack stack);
}