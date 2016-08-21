package seia.vanillamagic.items;

import java.util.ArrayList;
import java.util.List;

import seia.vanillamagic.items.accelerationcrystal.ItemAccelerationCrystal;

public class VanillaMagicItems 
{
	public static final VanillaMagicItems INSTANCE = new VanillaMagicItems();
	
	private List<ICustomItem> customItems = new ArrayList<ICustomItem>();
	
	public final ICustomItem itemAccelerationCrystal;
	
	private VanillaMagicItems()
	{
		itemAccelerationCrystal = new ItemAccelerationCrystal();
		customItems.add(itemAccelerationCrystal);
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