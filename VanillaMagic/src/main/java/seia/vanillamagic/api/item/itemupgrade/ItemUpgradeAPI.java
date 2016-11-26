package seia.vanillamagic.api.item.itemupgrade;

import java.lang.reflect.Method;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import seia.vanillamagic.api.VanillaMagicAPI;

public class ItemUpgradeAPI 
{
	private ItemUpgradeAPI()
	{
	}
	
	/**
	 * Main method for registering the upgrades.
	 * 
	 * @param itemMappingName -> For instance: "_pickaxe" or "_sword" or "_axe" or "_myNewMapping" itd.
	 * @param clazz -> MyUpgradeClass.class
	 */
	public static void addUpgradeMapping(String mappingName, Class<? extends IItemUpgrade> clazz)
	{
		try
		{
			Class<?> registryClass = Class.forName("seia.vanillamagic.item.itemupgrade.ItemUpgradeRegistry");
			Method method = registryClass.getMethod("addUpgradeMapping", String.class, IItemUpgrade.class);
			method.invoke(null, mappingName, clazz);
		}
		catch(Exception e)
		{
			VanillaMagicAPI.LOGGER.log(Level.ERROR, "Couldn't register upgrade class: " + clazz.getSimpleName());
			e.printStackTrace();
		}
	}
	
	/**
	 * Add a single value to the mapping. If mapping doesn't exists it will add a new mapping. <br>
	 * For instance: addItemToMapping("_pickaxe", new MyItemPickaxe()); <br>
	 * or <br>
	 * addItemToMapping("_myMapping", new MyItem());
	 */
	public static void addItemToMapping(String mappingName, Item item)
	{
		try
		{
			Class<?> registryClass = Class.forName("seia.vanillamagic.item.itemupgrade.ItemUpgradeRegistry");
			Method method = registryClass.getMethod("addItemToMapping", String.class, Item.class);
			method.invoke(null, mappingName, item);
		}
		catch(Exception e)
		{
			VanillaMagicAPI.LOGGER.log(Level.ERROR, "Couldn't register item: " + item.toString() + ", in mapping: " + mappingName);
			e.printStackTrace();
		}
	}
	
	/**
	 * Add a new mapping. <br>
	 * In VanillaMagic mapping will start with "_" so "pickaxe" will be -> "_pickaxe". <br>
	 * It was made this way to prevent "pickaxe" and "axe" being counted as one mapping.
	 */
	public static void addItemMapping(String mappingName)
	{
		try
		{
			Class<?> registryClass = Class.forName("seia.vanillamagic.item.itemupgrade.ItemUpgradeRegistry");
			Method method = registryClass.getMethod("addItemMapping", String.class);
			method.invoke(null, mappingName);
		}
		catch(Exception e)
		{
			VanillaMagicAPI.LOGGER.log(Level.ERROR, "Couldn't register item mapping: " + mappingName);
			e.printStackTrace();
		}
	}
	
	/**
	 * @param base -> Basic item. For instance: pickaxe
	 * @param ingredient -> Item needed to upgrade.
	 * @return ItemStack with upgrade and written NBT data.
	 */
	@Nullable
	public static ItemStack getResult(ItemStack base, ItemStack ingredient)
	{
		try
		{
			Class<?> registryClass = Class.forName("seia.vanillamagic.item.itemupgrade.ItemUpgradeRegistry");
			Method method = registryClass.getMethod("getResult", ItemStack.class, ItemStack.class);
			return (ItemStack) method.invoke(null, base, ingredient);
		}
		catch(Exception e)
		{
			VanillaMagicAPI.LOGGER.log(Level.ERROR, "Couldn't get result.");
			e.printStackTrace();
			return null;
		}
	}
	
	public static String getMappingNameFromItemStack(ItemStack stack)
	{
		try
		{
			Class<?> registryClass = Class.forName("seia.vanillamagic.item.itemupgrade.ItemUpgradeRegistry");
			Method method = registryClass.getMethod("getMappingNameFromItemStack", ItemStack.class);
			return (String) method.invoke(null, stack);
		}
		catch(Exception e)
		{
			VanillaMagicAPI.LOGGER.log(Level.ERROR, "Couldn't get mapping from stack: " + stack.toString());
			e.printStackTrace();
			return "";
		}
	}
}