package seia.vanillamagic.items;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import seia.vanillamagic.VanillaMagic;
import seia.vanillamagic.items.accelerationcrystal.ItemAccelerationCrystal;
import seia.vanillamagic.items.enchantedbucket.IEnchantedBucket;
import seia.vanillamagic.items.liquidsuppressioncrystal.ItemLiquidSuppressionCrystal;
import seia.vanillamagic.items.potionedcrystal.IPotionedCrystal;

public class VanillaMagicItems 
{
	public static final VanillaMagicItems INSTANCE = new VanillaMagicItems();
	
	private final List<ICustomItem> customItems;
	
	public final List<IEnchantedBucket> enchantedBuckets;
	public final List<IPotionedCrystal> potionedCrystals;
	public final ICustomItem itemAccelerationCrystal;
	public final ICustomItem itemLiquidSuppressionCrystal;
	
	private VanillaMagicItems()
	{
		customItems = new ArrayList<ICustomItem>();
		enchantedBuckets = new ArrayList<IEnchantedBucket>();
		potionedCrystals = new ArrayList<IPotionedCrystal>();
		
		itemAccelerationCrystal = new ItemAccelerationCrystal();
		customItems.add(itemAccelerationCrystal);
		
		itemLiquidSuppressionCrystal = new ItemLiquidSuppressionCrystal();
		customItems.add(itemLiquidSuppressionCrystal);
	}
	
	public void postInit()
	{
		for(ICustomItem customItem : customItems)
		{
			customItem.registerRecipe();
		}
		VanillaMagic.logger.log(Level.INFO, "Custom items registered: " + customItems.size());
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
	
	public boolean isCustomBucket(ItemStack checkingStack, IEnchantedBucket customBucket)
	{
		if(checkingStack == null || customBucket == null)
		{
			return false;
		}
		NBTTagCompound stackTag = checkingStack.getTagCompound();
		if(stackTag == null)
		{
			return false;
		}
		if(stackTag.hasKey(IEnchantedBucket.NBT_ENCHANTED_BUCKET))
		{
			if(stackTag.getString(IEnchantedBucket.NBT_ENCHANTED_BUCKET).equals(customBucket.getUniqueNBTName()))
			{
				if(stackTag.getString(IEnchantedBucket.NBT_FLUID_NAME).equals(customBucket.getFluidInBucket().getName()))
				{
					return true;
				}
			}
		}
		return false;
	}
}