package seia.vanillamagic.api.item;

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
	 * @see IVanillaMagicItems#addCustomItem(ICustomItem)
	 */
	public static void addCustomItem(ICustomItem item)
	{
		try
		{
			Class<?> clazz = Class.forName("seia.vanillamagic.item.VanillaMagicItems");
			Object instance = clazz.getField("INSTANCE").get(null);
			IVanillaMagicItems vmi = (IVanillaMagicItems) instance;
			vmi.addCustomItem(item);
			VanillaMagicAPI.LOGGER.log(Level.INFO, "Registered CustomItem: " + item.getUniqueNBTName());
		}
		catch(Exception e)
		{
			VanillaMagicAPI.LOGGER.log(Level.ERROR, "Error when registering CustomItem: " + item.getUniqueNBTName());
			e.printStackTrace();
		}
	}
	
	/**
	 * @see IVanillaMagicItems#isCustomItem(ItemStack, ICustomItem)
	 */
	public static boolean isCustomItem(ItemStack checkingStack, ICustomItem customItem)
	{
		try
		{
			Class<?> clazz = Class.forName("seia.vanillamagic.item.VanillaMagicItems");
			Object instance = clazz.getField("INSTANCE").get(null);
			IVanillaMagicItems vmi = (IVanillaMagicItems) instance;
			return vmi.isCustomItem(checkingStack, customItem);
		}
		catch(Exception e)
		{
			VanillaMagicAPI.LOGGER.log(Level.ERROR, "Error when checking for CustomItem.");
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * @see IVanillaMagicItems#isCustomBucket(ItemStack, IEnchantedBucket)
	 */
	public static boolean isCustomBucket(ItemStack checkingStack, IEnchantedBucket customBucket)
	{
		try
		{
			Class<?> clazz = Class.forName("seia.vanillamagic.item.VanillaMagicItems");
			Object instance = clazz.getField("INSTANCE").get(null);
			IVanillaMagicItems vmi = (IVanillaMagicItems) instance;
			return vmi.isCustomBucket(checkingStack, customBucket);
		}
		catch(Exception e)
		{
			VanillaMagicAPI.LOGGER.log(Level.ERROR, "Error when checking for EnchantedBucket.");
			e.printStackTrace();
			return false;
		}
	}
}