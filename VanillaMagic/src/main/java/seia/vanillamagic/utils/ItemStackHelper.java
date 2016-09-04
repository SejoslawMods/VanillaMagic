package seia.vanillamagic.utils;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import seia.vanillamagic.VanillaMagic;

public class ItemStackHelper 
{
	private ItemStackHelper()
	{
	}
	
	public static ItemStack getLapis(int amount)
	{
		return new ItemStack(Items.DYE, amount, 4);
	}
	
	public static ItemStack getBonemeal(int amount)
	{
		return new ItemStack(Items.DYE, amount, 0);
	}
	
	public static ItemStack getSugarCane(int amount)
	{
		return new ItemStack(Items.REEDS, amount);
	}

	/**
	 * 0 - Skeleton
	 * 1 - Wither Skeleton
	 * 2 - Zombie
	 * 3 - Steve
	 * 4 - Creeper
	 * 5 - Ender Dragon
	 */
	@Nullable
	public static ItemStack getHead(int amount, int meta)
	{
		return new ItemStack(Items.SKULL, amount, meta);
	}
	
	public static boolean checkItemsInHands(EntityPlayer player, 
			ItemStack shouldHaveInOffHand, ItemStack shouldHaveInMainHand)
	{
		ItemStack offHand = player.getHeldItemOffhand();
		ItemStack mainHand = player.getHeldItemMainhand();
		if(ItemStack.areItemStacksEqual(offHand, shouldHaveInOffHand))
		{
			if(ItemStack.areItemStacksEqual(mainHand, shouldHaveInMainHand))
			{
				return true;
			}
		}
		return false;
	}
	
	public static boolean printItemStackInfo(ItemStack stack, String[] additionalInfo)
	{
		if(stack != null)
		{
			VanillaMagic.logger.log(Level.INFO, "ItemStack Info");
			VanillaMagic.logger.log(Level.INFO, "Item = " + stack.getItem().toString());
			VanillaMagic.logger.log(Level.INFO, "StackSize = " + stack.stackSize);
			VanillaMagic.logger.log(Level.INFO, "ItemDamage = " + stack.getItemDamage());
			for(int i = 0; i < additionalInfo.length; i++)
			{
				VanillaMagic.logger.log(Level.INFO, "Additional Info #" + i + " = " + additionalInfo[i]);
			}
		}
		return false;
	}
	
	@Nullable
	public static ItemStack getItemStackWithNextMetadata(ItemStack stack)
	{
		ItemStack stackWithNextMeta = null;
		Item item = stack.getItem();
		int amount = stack.stackSize;
		int meta = stack.getItemDamage();
		try
		{
			stackWithNextMeta = new ItemStack(item, amount, meta + 1);
		}
		catch(Exception e)
		{
			stackWithNextMeta = new ItemStack(item, amount, 0);
		}
		return stackWithNextMeta;
	}
	
	public static ItemStack replaceItemInStack(ItemStack stack, Item item)
	{
		ItemStack newStack = stack.copy();
		newStack.setItem(item);
		return newStack;
	}
	
	@Nullable
	public static ItemStack getItemStackFromJSON(JsonObject jo)
	{
		try
		{
			int id = jo.get("id").getAsInt();
			int stackSize = jo.get("stackSize").getAsInt();
			int meta = jo.get("meta").getAsInt();
			Item item = null;
			Block block = null;
			try
			{
				item = Item.getItemById(id);
			}
			catch(Exception e)
			{
				block = Block.getBlockById(id);
			}
			
			if(item == null)
			{
				return new ItemStack(block, stackSize, meta);
			}
			else
			{
				return new ItemStack(item, stackSize, meta);
			}
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	public static ItemStack[] getItemStackArrayFromJSON(JsonObject jo, String key)
	{
		JsonArray ja = jo.get(key).getAsJsonArray();
		ItemStack[] tab = new ItemStack[ja.size()];
		int index = 0;
		ItemStack stack = null;
		for(JsonElement je : ja)
		{
			stack = getItemStackFromJSON(je.getAsJsonObject());
			tab[index] = stack;
			index++;
		}
		return tab;
	}
}