package seia.vanillamagic.util;

import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;

/**
 * TODO: Wait for ForgeTeam to remap name ItemStack.func_190916_E() to something like "getStackSize()"
 */
public class ItemStackHelper 
{
	/**
	 * new ItemStack((Item)null);
	 */
	public static final ItemStack NULL_STACK = ItemStack.field_190927_a;
	
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
	
	public static ItemStack replaceItemInStack(ItemStack stack, Item newItem)
	{
		ItemStack newStack = stack.copy();
		newStack.setItem(newItem);
		return newStack;
	}
	
	@Nullable
	public static ItemStack getItemStackFromJSON(JsonObject jo)
	{
		try
		{
			int id = jo.get("id").getAsInt();
			int stackSize = (jo.get("stackSize") != null ? jo.get("stackSize").getAsInt() : 1);
			int meta = (jo.get("meta") != null ? jo.get("meta").getAsInt() : 0);
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
			else if(block == null)
			{
				return new ItemStack(item, stackSize, meta);
			}
		}
		catch(Exception e)
		{
		}
		return null;
	}
	
	public static ItemStack[] getItemStackArrayFromJSON(JsonObject jo, String key)
	{
		JsonArray ja = jo.get(key).getAsJsonArray();
		ItemStack[] tab = new ItemStack[ja.size()];
		for(int i = 0; i < tab.length; i++)
		{
			JsonElement je = ja.get(i);
			ItemStack stack = getItemStackFromJSON(je.getAsJsonObject());
			tab[i] = stack;
		}
		return tab;
	}

	public static boolean isIInventory(ItemStack stack) 
	{
		if(stack == null)
		{
			return false;
		}
		Item itemFromStack = stack.getItem();
		Block blockFromStack = Block.getBlockFromItem(itemFromStack);
		if(blockFromStack == null)
		{
			return false;
		}
		if(blockFromStack instanceof ITileEntityProvider)
		{
			IBlockState blockFromStackState = blockFromStack.getDefaultState();
			TileEntity tileFromStack = blockFromStack.createTileEntity(null, blockFromStackState);
			if((tileFromStack instanceof IInventory) ||
					(tileFromStack instanceof IItemHandler))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Will return {@link ItemStackHelper#NULL_STACK} 
	 * if the {@link ItemStack} should be understand as Empty.
	 */
	public static ItemStack loadItemStackFromNBT(NBTTagCompound tag)
	{
		ItemStack stack = new ItemStack(tag);
		if(stack.func_190926_b())
		{
			return NULL_STACK;
		}
		return stack;
	}
}