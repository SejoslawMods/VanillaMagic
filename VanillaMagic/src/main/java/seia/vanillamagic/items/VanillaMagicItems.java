package seia.vanillamagic.items;

import java.util.ArrayList;
import java.util.List;

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
		System.out.println("Custom items registered");
	}
}