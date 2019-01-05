package seia.vanillamagic.util;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;
import seia.vanillamagic.core.VanillaMagic;

/**
 * Class which store various methods connected with ItemStack.
 */
public class ItemStackUtil
{
	/**
	 * new ItemStack((Item)null);
	 */
	public static final ItemStack NULL_STACK = ItemStack.EMPTY;
	
	private ItemStackUtil()
	{
	}
	
	/**
	 * @return Returns Lapis item.
	 */
	public static ItemStack getLapis(int amount)
	{
		return new ItemStack(Items.DYE, amount, 4);
	}
	
	/**
	 * @return Returns Bonemeal item.
	 */
	public static ItemStack getBonemeal(int amount)
	{
		return new ItemStack(Items.DYE, amount, 0);
	}
	
	/**
	 * @return Returns Sugar Cane item.
	 */
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
	
	/**
	 * @return Returns TRUE if given stack's name is equal to selected name.
	 */
	public static boolean stackNameEqual(ItemStack stack, String name)
	{
		return stack.getDisplayName().equalsIgnoreCase(name);
	}
	
	/**
	 * @return Returns TRUE if Player holds right items in hands.
	 */
	public static boolean checkItemsInHands(EntityPlayer player, ItemStack shouldHaveInOffHand, ItemStack shouldHaveInMainHand)
	{
		// Check Player
		if (player == null) return false;
		
		// Check Left Hand
		ItemStack offHand = player.getHeldItemOffhand();
		if (shouldHaveInOffHand != null) // Not null means that we want to check this hand
		{
			if (isNullStack(offHand) ||
					ItemStackUtil.getStackSize(offHand) != ItemStackUtil.getStackSize(shouldHaveInOffHand) ||
					offHand.getItem() != shouldHaveInOffHand.getItem() ||
					offHand.getMetadata() != shouldHaveInOffHand.getMetadata())
			{
				return false;
			}
		}
		// Check Right Hand
		ItemStack mainHand = player.getHeldItemMainhand();
		if (shouldHaveInMainHand != null) // Not null means that we want to check this hand
		{
			if (isNullStack(mainHand) ||
					ItemStackUtil.getStackSize(mainHand) != ItemStackUtil.getStackSize(shouldHaveInMainHand) ||
					mainHand.getItem() != shouldHaveInMainHand.getItem() ||
					mainHand.getMetadata() != shouldHaveInMainHand.getMetadata())
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @return Returns the same stack but with next metadata.
	 */
	@Nullable
	public static ItemStack getItemStackWithNextMetadata(ItemStack stack)
	{
		ItemStack stackWithNextMeta = null;
		Item item = stack.getItem();
		int amount = getStackSize(stack); // stackSize
		int meta = stack.getItemDamage();
		try
		{
			stackWithNextMeta = new ItemStack(item, amount, meta + 1);
		}
		catch (Exception e)
		{
			stackWithNextMeta = new ItemStack(item, amount, 0);
		}
		return stackWithNextMeta;
	}
	
	/**
	 * @return Returns ItemStack from JSON Object.
	 */
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
			catch (Exception e)
			{
				block = Block.getBlockById(id);
			}
			
			if (item == null) return new ItemStack(block, stackSize, meta);
			else if (block == null) return new ItemStack(item, stackSize, meta);
		}
		catch (Exception e)
		{
		}
		return null;
	}
	
	/**
	 * @return Returns readded ItemStack array from JSON Object.
	 */
	public static ItemStack[] getItemStackArrayFromJSON(JsonObject jo, String key)
	{
		JsonArray ja = jo.get(key).getAsJsonArray();
		ItemStack[] tab = new ItemStack[ja.size()];
		for (int i = 0; i < tab.length; ++i)
		{
			JsonElement je = ja.get(i);
			ItemStack stack = getItemStackFromJSON(je.getAsJsonObject());
			tab[i] = stack;
		}
		return tab;
	}
	
	/**
	 * @return Returns TRUE if given stack is an inventory.
	 */
	public static boolean isIInventory(ItemStack stack) 
	{
		if (ItemStackUtil.isNullStack(stack)) return false;
		
		Item itemFromStack = stack.getItem();
		Block blockFromStack = Block.getBlockFromItem(itemFromStack);
		if (blockFromStack == null) return false;
		
		if (blockFromStack instanceof ITileEntityProvider)
		{
			IBlockState blockFromStackState = blockFromStack.getDefaultState();
			TileEntity tileFromStack = blockFromStack.createTileEntity(null, blockFromStackState);
			if ((tileFromStack instanceof IInventory) ||
					(tileFromStack instanceof IItemHandler))
			{
				return true;
			}
		}
		return false;
	}
	
	//================================== StackSize Operations ====================================
	
	/**
	 * @return Will return {@link ItemStackHelper#NULL_STACK} 
	 * if the {@link ItemStack} should be understand as Empty.
	 */
	public static ItemStack loadItemStackFromNBT(NBTTagCompound tag)
	{
		ItemStack stack = new ItemStack(tag);
		if (stack.isEmpty()) return NULL_STACK;
		return stack;
	}
	
	/**
	 * @return Returns the old ItemStack.stackSize
	 */
	public static int getStackSize(ItemStack stack)
	{
		if (stack == null) return 0;
		return stack.getCount();
	}
	
	/**
	 * This method will sets the ItemStack.stackSize to the given value.<br>
	 * @return Returns the given stack.
	 */
	public static void setStackSize(ItemStack stack, int value)
	{
		if (stack == null) return;
		stack.setCount(value);
	}
	
	/**
	 * This method will increase the ItemStack.stackSize of the given stack.<br>
	 * @return Returns the given stack.
	 */
	public static void increaseStackSize(ItemStack stack, int value)
	{
		if (stack == null) return;
		stack.grow(value);
	}
	
	/**
	 * This method will decrease the ItemStack.stackSize of the given stack.<br>
	 * @return Returns the given stack.
	 */
	public static void decreaseStackSize(ItemStack stack, int value)
	{
		if (stack == null) return;
		stack.shrink(value);
	}
	
	/**
	 * @return Returns true if the given ItemStack is {@link #NULL_STACK}
	 */
	public static boolean isNullStack(ItemStack stack)
	{
		if (stack == null) return true;
		return stack.isEmpty();
	}
	
	/**
	 * Prints stack data.
	 */
	public static void printStack(ItemStack stack) 
	{
		VanillaMagic.LOGGER.log(Level.INFO, "Printing ItemStack data...");
		VanillaMagic.LOGGER.log(Level.INFO, "Item: " + stack.getItem());
		VanillaMagic.LOGGER.log(Level.INFO, "StackSize: " + getStackSize(stack));
		VanillaMagic.LOGGER.log(Level.INFO, "Meta: " + stack.toString());
	}
}