package seia.vanillamagic.inventory;

import java.lang.reflect.Method;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import seia.vanillamagic.api.inventory.IInventoryWrapper;
import seia.vanillamagic.util.ItemStackHelper;

public class InventoryHelper 
{
	private static Class<?> CLASS_TILE_HOPPER = null;
	
	private InventoryHelper()
	{
	}
	
	static
	{
		try 
		{
			CLASS_TILE_HOPPER = Class.forName("net.minecraft.tileentity.TileEntityHopper");
		} 
		catch(ClassNotFoundException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static boolean isInventory(World world, BlockPos pos) 
	{
		return world.getTileEntity(pos) instanceof IInventory;
	}
	
	public static void dropInventoryItems(World worldIn, BlockPos pos, IInventory inventory)
	{
		net.minecraft.inventory.InventoryHelper.dropInventoryItems(worldIn, pos, inventory);
	}
	
	public static void dropInventoryItems(World worldIn, Entity entityAt, IInventory inventory)
	{
		net.minecraft.inventory.InventoryHelper.dropInventoryItems(worldIn, entityAt, inventory);
	}
	
	public static void spawnItemStack(World worldIn, BlockPos pos, ItemStack stack)
	{
		net.minecraft.inventory.InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
	}
	
	public static IItemHandler createUnSidedHandler(IInventory inv)
	{
		return new InvWrapper(inv);
	}
	
	/**
	 * Returns false if the inventory has any room to place items in
	 */
	public static boolean isInventoryFull(IInventory inventoryIn, EnumFacing side)
	{
		if(inventoryIn instanceof ISidedInventory)
		{
			ISidedInventory isidedinventory = (ISidedInventory)inventoryIn;
			int[] slots = isidedinventory.getSlotsForFace(side);
			for(int slot : slots)
			{
				ItemStack stack = isidedinventory.getStackInSlot(slot);
				if(ItemStackHelper.isNullStack(stack) || ItemStackHelper.getStackSize(stack) != stack.getMaxStackSize())
				{
					return false;
				}
			}
		}
		else
		{
			int slots = inventoryIn.getSizeInventory();
			for(int slot = 0; slot < slots; ++slot)
			{
				ItemStack stack = inventoryIn.getStackInSlot(slot);
				if(ItemStackHelper.isNullStack(stack) || ItemStackHelper.getStackSize(stack) != stack.getMaxStackSize())
				{
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Returns true if the given inventory has free slot.
	 */
	public static boolean hasInventoryFreeSpace(IInventory inventoryIn, EnumFacing side)
	{
		if(inventoryIn instanceof ISidedInventory)
		{
			ISidedInventory iSidedInventory = (ISidedInventory)inventoryIn;
			int[] slots = iSidedInventory.getSlotsForFace(side);
			for(int k : slots)
			{
				ItemStack stack = iSidedInventory.getStackInSlot(k);
				if(stack == null)
				{
					return true;
				}
			}
		}
		else
		{
			int size = inventoryIn.getSizeInventory();
			for(int j = 0; j < size; ++j)
			{
				ItemStack stack = inventoryIn.getStackInSlot(j);
				if(stack == null)
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns false if the specified IInventory contains any items
	 */
	public static boolean isInventoryEmpty(IInventory inventoryIn, EnumFacing side)
			throws ReflectiveOperationException
	{
		Method method = CLASS_TILE_HOPPER.getMethod("isInventoryEmpty", IInventory.class, EnumFacing.class);
		method.setAccessible(true);
		return (boolean) method.invoke(null, inventoryIn, side);
	}

	public static boolean captureDroppedItems(IHopper hopper)
	{
		return TileEntityHopper.captureDroppedItems(hopper);
	}

	/**
	 * Pulls from the specified slot in the inventoryIn and places in any available slot in the inventoryOut. Returns true if
	 * the entire stack was moved
	 */
	public static boolean pullItemFromSlot(IInventory inventoryOut, IInventory inventoryIn, int index, EnumFacing direction)
	{
		ItemStack stack = inventoryIn.getStackInSlot(index);
		if(stack != null && canExtractItemFromSlot(inventoryIn, stack, index, direction))
		{
			ItemStack stackCopy = stack.copy();
			ItemStack leftItems = putStackInInventoryAllSlots(inventoryOut, inventoryIn.decrStackSize(index, 1), (EnumFacing)null);
			if(leftItems == null || /*leftItems.stackSize*/ ItemStackHelper.getStackSize(leftItems)  == 0)
			{
				inventoryIn.markDirty();
				return true;
			}
			inventoryIn.setInventorySlotContents(index, stackCopy);
		}
		return false;
	}

	/**
	 * Attempts to place the passed EntityItem's stack into the inventory using as many slots as possible. Returns false
	 * if the stackSize of the drop was not depleted.
	 */
	public static boolean putDropInInventoryAllSlots(IInventory inventoryIn, EntityItem itemIn)
	{
		return TileEntityHopper.putDropInInventoryAllSlots(null, inventoryIn, itemIn);
	}

	/**
	 * Attempts to place the passed stack in the inventory, using as many slots as required. Returns leftover items
	 */
	@Nullable
	public static ItemStack putStackInInventoryAllSlots(IInventory inventoryIn, ItemStack stack, @Nullable EnumFacing side)
	{
		return TileEntityHopper.putStackInInventoryAllSlots(null, inventoryIn, stack, side);
	}

	/**
	 * Can insert the specified item from the specified slot on the specified side?
	 */
	public static boolean canInsertItemInSlot(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side)
	{
		return !inventoryIn.isItemValidForSlot(index, stack) ? 
				false : 
					!(inventoryIn instanceof ISidedInventory) || ((ISidedInventory)inventoryIn).canInsertItem(index, stack, side);
	}

	/**
	 * Can extract the specified item from the specified slot on the specified side?
	 */
	public static boolean canExtractItemFromSlot(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side)
	{
		return !(inventoryIn instanceof ISidedInventory) || ((ISidedInventory)inventoryIn).canExtractItem(index, stack, side);
	}

	/**
	 * Insert the specified stack to the specified inventory and return any leftover items
	 */
	@Nullable
	public static ItemStack insertStack(IInventory inventoryIn, IInventory stack, ItemStack index, int side, EnumFacing facing)
			throws ReflectiveOperationException
	{
		Method method = CLASS_TILE_HOPPER.getMethod("insertStack", IInventory.class, IInventory.class, ItemStack.class, int.class, EnumFacing.class);
		method.setAccessible(true);
		return (ItemStack) method.invoke(null, inventoryIn, stack, index, side, facing);
	}

	/**
	 * Returns the IInventory for the specified hopper
	 */
	public static IInventory getHopperInventory(IHopper hopper)
	{
		return getInventoryAtPosition(hopper.getWorld(), hopper.getXPos(), hopper.getYPos() + 1.0D, hopper.getZPos());
	}

	@Nullable
	public static List<EntityItem> getCaptureItems(World worldIn, double x, double y, double z)
	{
		return TileEntityHopper.getCaptureItems(worldIn, x, y, z);
	}
    
	@Nullable
	public static IInventory getInventoryAtPosition(World world, BlockPos pos)
	{
		return getInventoryAtPosition(world, pos.getX(), pos.getY(), pos.getZ());
	}

	/**
	 * Returns the IInventory (if applicable) of the TileEntity at the specified position
	 */
	@Nullable
	public static IInventory getInventoryAtPosition(World worldIn, double x, double y, double z)
	{
		return TileEntityHopper.getInventoryAtPosition(worldIn, x, y, z);
	}

	public static boolean canCombine(ItemStack stack1, ItemStack stack2) 
			throws ReflectiveOperationException
	{
		Method method = CLASS_TILE_HOPPER.getMethod("canCombine", ItemStack.class, ItemStack.class);
		method.setAccessible(true);
		return (boolean) method.invoke(null, stack1, stack2);
	}

	public static int getFirstNotNull(IInventory inv) 
	{
		for(int i = 0; i < inv.getSizeInventory(); i++)
		{
			if(!ItemStackHelper.isNullStack(inv.getStackInSlot(i)))
			{
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Returns the slot number of the first found inventory.<br>
	 * If there is no inventory block, will return -1
	 */
	public static int containsAnotherInventoryBlock(IInventoryWrapper wrapper)
	{
		IInventory inv = wrapper.getInventory();
		for(int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack checkingStack = inv.getStackInSlot(i);
			if(checkingStack != null)
			{
				Item checkingItem = checkingStack.getItem();
				Block blockFromItem = Block.getBlockFromItem(checkingItem);
				if(blockFromItem != null)
				{
					IBlockState blockFromItemState = blockFromItem.getDefaultState();
					if(blockFromItem.hasTileEntity(blockFromItemState))
					{
						TileEntity tileFromBlock = blockFromItem.createTileEntity(wrapper.getWorld(), blockFromItemState);
						if(tileFromBlock instanceof IInventory)
						{
							return i;
						}
					}
				}
			}
		}
		return -1;
	}
}