package seia.vanillamagic.item;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import seia.vanillamagic.VanillaMagic;
import seia.vanillamagic.api.item.ICustomItem;
import seia.vanillamagic.api.item.IEnchantedBucket;
import seia.vanillamagic.api.item.IVanillaMagicItems;
import seia.vanillamagic.item.accelerationcrystal.ItemAccelerationCrystal;
import seia.vanillamagic.item.liquidsuppressioncrystal.ItemLiquidSuppressionCrystal;
import seia.vanillamagic.item.potionedcrystal.IPotionedCrystal;
import seia.vanillamagic.item.thecrystalofmothernature.ItemMotherNatureCrystal;

public class VanillaMagicItems implements IVanillaMagicItems
{
	public static final VanillaMagicItems INSTANCE = new VanillaMagicItems();
	
	private final List<ICustomItem> customItems;
	
	public final List<IEnchantedBucket> enchantedBuckets;
	public final List<IPotionedCrystal> potionedCrystals;
	public final ICustomItem itemAccelerationCrystal;
	public final ICustomItem itemLiquidSuppressionCrystal;
	public final ICustomItem itemMotherNatureCrystal;
	
	private VanillaMagicItems()
	{
		customItems = new ArrayList<ICustomItem>();
		enchantedBuckets = new ArrayList<IEnchantedBucket>();
		potionedCrystals = new ArrayList<IPotionedCrystal>();
		
		itemAccelerationCrystal = new ItemAccelerationCrystal();
		customItems.add(itemAccelerationCrystal);
		
		itemLiquidSuppressionCrystal = new ItemLiquidSuppressionCrystal();
		customItems.add(itemLiquidSuppressionCrystal);
		
		itemMotherNatureCrystal = new ItemMotherNatureCrystal();
		customItems.add(itemMotherNatureCrystal);
	}
	
	public void addCustomItem(ICustomItem item)
	{
		customItems.add(item);
	}
	
	public void postInit()
	{
		for(ICustomItem customItem : customItems)
		{
			customItem.registerRecipe();
		}
		VanillaMagic.LOGGER.log(Level.INFO, "Custom items registered: " + customItems.size());
	}
	
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