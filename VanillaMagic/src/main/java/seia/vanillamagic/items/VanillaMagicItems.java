package seia.vanillamagic.items;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import seia.vanillamagic.VanillaMagic;
import seia.vanillamagic.items.accelerationcrystal.ItemAccelerationCrystal;
import seia.vanillamagic.items.enchantedbucket.EnchantedBucketLava;
import seia.vanillamagic.items.enchantedbucket.EnchantedBucketWater;

public class VanillaMagicItems 
{
	public static final VanillaMagicItems INSTANCE = new VanillaMagicItems();
	
	private List<ICustomItem> customItems = new ArrayList<ICustomItem>();
	
	public final ICustomItem itemAccelerationCrystal;
	public final ICustomItem itemEnchantedBucketWater;
	public final ICustomItem itemEnchantedBucketLava;
	
	private VanillaMagicItems()
	{
		itemAccelerationCrystal = new ItemAccelerationCrystal();
		customItems.add(itemAccelerationCrystal);
		
		itemEnchantedBucketWater = new EnchantedBucketWater();
		customItems.add(itemEnchantedBucketWater);
		
		itemEnchantedBucketLava = new EnchantedBucketLava();
		customItems.add(itemEnchantedBucketLava);
	}
	
	public void postInit()
	{
		for(ICustomItem customItem : customItems)
		{
			customItem.registerRecipe();
		}
		VanillaMagic.logger.log(Level.INFO, "Custom items registered");
	}
	
	/**
	 * Returns true ONLY if the given stack is a given custom item.
	 */
	public boolean isCustomItem(ItemStack checkingStack, ICustomItem customItem)
	{
		if(checkingStack == null || customItem == null)
		{
			return false;
		}
		NBTTagCompound stackTag = checkingStack.getTagCompound();
		if(stackTag == null)
		{
			return false;
		}
		if(stackTag.hasKey(ICustomItem.NBT_UNIQUE_NAME))
		{
			if(stackTag.getString(ICustomItem.NBT_UNIQUE_NAME).equals(customItem.getUniqueNBTName()))
			{
				return true;
			}
		}
		return false;
	}
}