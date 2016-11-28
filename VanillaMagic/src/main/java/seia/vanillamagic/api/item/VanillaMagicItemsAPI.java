package seia.vanillamagic.api.item;

import java.lang.reflect.Method;

import org.apache.logging.log4j.Level;

import net.minecraft.item.ItemStack;
import seia.vanillamagic.api.VanillaMagicAPI;

/**
 * This is Your connection to VanillaMagicItems functionality.
 */
public class VanillaMagicItemsAPI
{
	private VanillaMagicItemsAPI()
	{
	}
	
	/**
	 * Adds Custom Item to the Registry.
	 */
	public static void addCustomItem(ICustomItem item)
	{
		try
		{
			Class<?> clazz = Class.forName("seia.vanillamagic.item.VanillaMagicItems");
			Method method = clazz.getMethod("addCustomItem", ICustomItem.class);
			method.invoke(null, item);
			VanillaMagicAPI.LOGGER.log(Level.INFO, "Registered CustomItem: " + item.getUniqueNBTName());
		}
		catch(Exception e)
		{
			VanillaMagicAPI.LOGGER.log(Level.ERROR, "Error when registering CustomItem: " + item.getUniqueNBTName());
			e.printStackTrace();
		}
	}
	
	/**
	 * @return Returns true ONLY if the given stack is a given custom item.
	 */
	public static boolean isCustomItem(ItemStack checkingStack, ICustomItem customItem)
	{
		try
		{
			Class<?> clazz = Class.forName("seia.vanillamagic.item.VanillaMagicItems");
			Method method = clazz.getMethod("isCustomItem", ItemStack.class, ICustomItem.class);
			return (boolean) method.invoke(null, checkingStack, customItem);
		}
		catch(Exception e)
		{
			VanillaMagicAPI.LOGGER.log(Level.ERROR, "Error when checking for CustomItem.");
			e.printStackTrace();
			return false;
		}
	}
}