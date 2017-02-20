package seia.vanillamagic.item;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import seia.vanillamagic.api.item.ICustomItem;
import seia.vanillamagic.api.item.IEnchantedBucket;
import seia.vanillamagic.api.item.IPotionedCrystal;
import seia.vanillamagic.core.VanillaMagic;
import seia.vanillamagic.item.accelerationcrystal.ItemAccelerationCrystal;
import seia.vanillamagic.item.evokercrystal.ItemEvokerCrystal;
import seia.vanillamagic.item.inventoryselector.ItemInventorySelector;
import seia.vanillamagic.item.liquidsuppressioncrystal.ItemLiquidSuppressionCrystal;
import seia.vanillamagic.item.thecrystalofmothernature.ItemMotherNatureCrystal;
import seia.vanillamagic.util.ItemStackHelper;

public class VanillaMagicItems
{
	/**
	 * All VanillaMagic items except these with additional lists.
	 */
	public static final List<ICustomItem> CUSTOM_ITEMS;
	/**
	 * All EnchantedBuckets list.
	 */
	public static final List<IEnchantedBucket> ENCHANTED_BUCKETS;
	/**
	 * All PotionedCrystals list.
	 */
	public static final List<IPotionedCrystal> POTIONED_CRYSTALS;
	
	public static final CustomItemCrystal ACCELERATION_CRYSTAL;
	public static final CustomItemCrystal LIQUID_SUPPRESSION_CRYSTAL; 
	public static final CustomItemCrystal MOTHER_NATURE_CRYSTAL; 
	public static final CustomItem INVENTORY_SELECTOR;
	public static final CustomItemCrystal EVOKER_CRYSTAL;
	
	private VanillaMagicItems()
	{
	}
	
	static
	{
		CUSTOM_ITEMS = new ArrayList<ICustomItem>();
		
		ENCHANTED_BUCKETS = new ArrayList<IEnchantedBucket>();
		POTIONED_CRYSTALS = new ArrayList<IPotionedCrystal>();
		
		ACCELERATION_CRYSTAL = new ItemAccelerationCrystal();
		CUSTOM_ITEMS.add(ACCELERATION_CRYSTAL);
		
		LIQUID_SUPPRESSION_CRYSTAL = new ItemLiquidSuppressionCrystal();
		CUSTOM_ITEMS.add(LIQUID_SUPPRESSION_CRYSTAL);
		
		MOTHER_NATURE_CRYSTAL = new ItemMotherNatureCrystal();
		CUSTOM_ITEMS.add(MOTHER_NATURE_CRYSTAL);
		
		INVENTORY_SELECTOR = new ItemInventorySelector();
		CUSTOM_ITEMS.add(INVENTORY_SELECTOR);
		
		EVOKER_CRYSTAL = new ItemEvokerCrystal();
		CUSTOM_ITEMS.add(EVOKER_CRYSTAL);
	}
	
	public static void addCustomItem(ICustomItem item)
	{
		CUSTOM_ITEMS.add(item);
	}
	
	public static void postInit()
	{
		for(ICustomItem customItem : CUSTOM_ITEMS)
		{
			customItem.registerRecipe();
		}
		VanillaMagic.LOGGER.log(Level.INFO, "Custom items registered: " + CUSTOM_ITEMS.size());
	}
	
	public static boolean isCustomItem(ItemStack checkingStack, ICustomItem customItem)
	{
		if(ItemStackHelper.isNullStack(checkingStack) || customItem == null)
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
	
	public static boolean isCustomBucket(ItemStack checkingStack, IEnchantedBucket customBucket)
	{
		if(ItemStackHelper.isNullStack(checkingStack) || customBucket == null)
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
	
	public static NonNullList<ItemStack> fillList(NonNullList<ItemStack> list) 
	{
		for(ICustomItem ci : CUSTOM_ITEMS)
		{
			list.add(ci.getItem());
		}
		for(IEnchantedBucket eb : ENCHANTED_BUCKETS)
		{
			list.add(eb.getItem());
		}
		for(IPotionedCrystal pc : POTIONED_CRYSTALS)
		{
			list.add(pc.getItem());
		}
		return list;
	}
}