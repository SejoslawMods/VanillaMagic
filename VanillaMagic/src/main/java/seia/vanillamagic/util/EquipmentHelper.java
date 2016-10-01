package seia.vanillamagic.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import seia.vanillamagic.VanillaMagic;

public class EquipmentHelper 
{
	public static final List<Item> HELMETS = new ArrayList<Item>();
	public static final List<Item> CHESTPLATES = new ArrayList<Item>();
	public static final List<Item> LEGGINGS = new ArrayList<Item>();
	public static final List<Item> BOOTS = new ArrayList<Item>();
	public static final List<Item> WEAPONS = new ArrayList<Item>();
	
	private EquipmentHelper()
	{
	}
	
	static
	{
		List<Item> items = ForgeRegistries.ITEMS.getValues();
		for(Item item : items)
		{
			if(item.getUnlocalizedName().contains("helmet") || item.getUnlocalizedName().contains("hat"))
			{
				HELMETS.add(item);
			}
		}
		VanillaMagic.LOGGER.log(Level.INFO, "Readded helmets: " + HELMETS.size());
		for(Item item : items)
		{
			if(item.getUnlocalizedName().contains("chestplate"))
			{
				CHESTPLATES.add(item);
			}
		}
		VanillaMagic.LOGGER.log(Level.INFO, "Readded chestplates: " + CHESTPLATES.size());
		for(Item item : items)
		{
			if(item.getUnlocalizedName().contains("leggings"))
			{
				LEGGINGS.add(item);
			}
		}
		VanillaMagic.LOGGER.log(Level.INFO, "Readded leggings: " + LEGGINGS.size());
		for(Item item : items)
		{
			if(item.getUnlocalizedName().contains("boots"))
			{
				BOOTS.add(item);
			}
		}
		VanillaMagic.LOGGER.log(Level.INFO, "Readded boots: " + BOOTS.size());
		for(Item item : items)
		{
			if(item.getUnlocalizedName().contains("sword") || item.getUnlocalizedName().contains("bow"))
			{
				WEAPONS.add(item);
			}
		}
		VanillaMagic.LOGGER.log(Level.INFO, "Readded weapons: " + WEAPONS.size());
	}
}