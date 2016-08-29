package seia.vanillamagic.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import seia.vanillamagic.VanillaMagic;

public class EquipmentHelper 
{
	public static final EquipmentHelper INSTANCE = new EquipmentHelper();
	
	public List<Item> helmets = new ArrayList<Item>();
	public List<Item> chestplates = new ArrayList<Item>();
	public List<Item> leggings = new ArrayList<Item>();
	public List<Item> boots = new ArrayList<Item>();
	public List<Item> weapons = new ArrayList<Item>();
	
	private EquipmentHelper()
	{
		List<Item> items = ForgeRegistries.ITEMS.getValues();
		for(Item item : items)
		{
			if(item.getUnlocalizedName().contains("helmet") || item.getUnlocalizedName().contains("hat"))
			{
				helmets.add(item);
			}
		}
		VanillaMagic.logger.log(Level.INFO, "Readded helmets: " + helmets.size());
		for(Item item : items)
		{
			if(item.getUnlocalizedName().contains("chestplate"))
			{
				chestplates.add(item);
			}
		}
		VanillaMagic.logger.log(Level.INFO, "Readded chestplates: " + chestplates.size());
		for(Item item : items)
		{
			if(item.getUnlocalizedName().contains("leggings"))
			{
				leggings.add(item);
			}
		}
		VanillaMagic.logger.log(Level.INFO, "Readded leggings: " + leggings.size());
		for(Item item : items)
		{
			if(item.getUnlocalizedName().contains("boots"))
			{
				boots.add(item);
			}
		}
		VanillaMagic.logger.log(Level.INFO, "Readded boots: " + boots.size());
		for(Item item : items)
		{
			if(item.getUnlocalizedName().contains("sword") || item.getUnlocalizedName().contains("bow"))
			{
				weapons.add(item);
			}
		}
		VanillaMagic.logger.log(Level.INFO, "Readded weapons: " + weapons.size());
	}
}