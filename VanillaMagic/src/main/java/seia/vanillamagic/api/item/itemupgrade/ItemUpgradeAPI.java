package seia.vanillamagic.api.item.itemupgrade;

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
	 * @see IItemUpgradeRegistry#addUpgradeMapping(String, Class)
	 */
	public static void addUpgradeMapping(String mappingName, Class<? extends IItemUpgrade> clazz)
	{
		try
		{
			Class<?> registryClass = Class.forName("seia.vanillamagic.item.itemupgrade.ItemUpgradeRegistry");
			Object instance = registryClass.getField("INSTANCE").get(null);
			IItemUpgradeRegistry registry = (IItemUpgradeRegistry) instance;
			registry.addUpgradeMapping(mappingName, clazz);
		}
		catch(Exception e)
		{
			VanillaMagicAPI.LOGGER.log(Level.ERROR, "Couldn't register upgrade class: " + clazz.getSimpleName());
			e.printStackTrace();
		}
	}
	
	/**
	 * @see IItemUpgradeRegistry#addItemToMapping(String, Item)
	 */
	public static void addItemToMapping(String mappingName, Item item)
	{
		try
		{
			Class<?> registryClass = Class.forName("seia.vanillamagic.item.itemupgrade.ItemUpgradeRegistry");
			Object instance = registryClass.getField("INSTANCE").get(null);
			IItemUpgradeRegistry registry = (IItemUpgradeRegistry) instance;
			registry.addItemToMapping(mappingName, item);
		}
		catch(Exception e)
		{
			VanillaMagicAPI.LOGGER.log(Level.ERROR, "Couldn't register item: " + item.toString() + ", in mapping: " + mappingName);
			e.printStackTrace();
		}
	}
	
	/**
	 * @see IItemUpgradeRegistry#addItemMapping(String)
	 */
	public static void addItemMapping(String mappingName)
	{
		try
		{
			Class<?> registryClass = Class.forName("seia.vanillamagic.item.itemupgrade.ItemUpgradeRegistry");
			Object instance = registryClass.getField("INSTANCE").get(null);
			IItemUpgradeRegistry registry = (IItemUpgradeRegistry) instance;
			registry.addItemMapping(mappingName);
		}
		catch(Exception e)
		{
			VanillaMagicAPI.LOGGER.log(Level.ERROR, "Couldn't register item mapping: " + mappingName);
			e.printStackTrace();
		}
	}
	
	/**
	 * @see IItemUpgradeRegistry#getResult(ItemStack, ItemStack)
	 */
	@Nullable
	public static ItemStack getResult(ItemStack base, ItemStack ingredient)
	{
		try
		{
			Class<?> registryClass = Class.forName("seia.vanillamagic.item.itemupgrade.ItemUpgradeRegistry");
			Object instance = registryClass.getField("INSTANCE").get(null);
			IItemUpgradeRegistry registry = (IItemUpgradeRegistry) instance;
			return registry.getResult(base, ingredient);
		}
		catch(Exception e)
		{
			VanillaMagicAPI.LOGGER.log(Level.ERROR, "Couldn't get result.");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * @see IItemUpgradeRegistry#getMappingNameFromItemStack(ItemStack)
	 */
	public static String getMappingNameFromItemStack(ItemStack stack)
	{
		try
		{
			Class<?> registryClass = Class.forName("seia.vanillamagic.item.itemupgrade.ItemUpgradeRegistry");
			Object instance = registryClass.getField("INSTANCE").get(null);
			IItemUpgradeRegistry registry = (IItemUpgradeRegistry) instance;
			return registry.getMappingNameFromItemStack(stack);
		}
		catch(Exception e)
		{
			VanillaMagicAPI.LOGGER.log(Level.ERROR, "Couldn't get mapping from stack: " + stack.toString());
			e.printStackTrace();
			return "";
		}
	}
}