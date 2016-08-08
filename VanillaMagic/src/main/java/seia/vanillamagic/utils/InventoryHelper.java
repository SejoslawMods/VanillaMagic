package seia.vanillamagic.utils;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.VanillaInventoryCodeHooks;
import net.minecraftforge.items.wrapper.InvWrapper;

public class InventoryHelper 
{
	private InventoryHelper()
	{
	}
	
	public static void dropInventoryItems(World worldIn, BlockPos pos, IInventory inventory)
	{
		net.minecraft.inventory.InventoryHelper.dropInventoryItems(worldIn, pos, inventory);
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
		if (inventoryIn instanceof ISidedInventory)
		{
			ISidedInventory iSidedInventory = (ISidedInventory)inventoryIn;
			int[] slots = iSidedInventory.getSlotsForFace(side);
			for (int k : slots)
			{
				ItemStack stack = iSidedInventory.getStackInSlot(k);
				if (stack == null || stack.stackSize != stack.getMaxStackSize())
				{
					return false;
				}
			}
		}
		else
		{
			int size = inventoryIn.getSizeInventory();
			for (int j = 0; j < size; ++j)
			{
				ItemStack stack = inventoryIn.getStackInSlot(j);
				if (stack == null || stack.stackSize != stack.getMaxStackSize())
				{
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Returns false if the specified IInventory contains any items
	 */
	public static boolean isInventoryEmpty(IInventory inventoryIn, EnumFacing side)
	{
		if (inventoryIn instanceof ISidedInventory)
		{
			ISidedInventory iSidedInventory = (ISidedInventory)inventoryIn;
			int[] slots = iSidedInventory.getSlotsForFace(side);
			for (int i : slots)
			{
				if (iSidedInventory.getStackInSlot(i) != null)
				{
					return false;
				}
			}
		}
		else
		{
			int size = inventoryIn.getSizeInventory();
			for (int k = 0; k < size; ++k)
			{
				if (inventoryIn.getStackInSlot(k) != null)
				{
					return false;
				}
			}
		}
		return true;
	}

	public static boolean captureDroppedItems(IHopper hopper)
	{
		Boolean ret = VanillaInventoryCodeHooks.extractHook(hopper);
		if (ret != null)
		{
			return ret;
		}
		IInventory iInventory = getHopperInventory(hopper);
		if (iInventory != null)
		{
			EnumFacing facing = EnumFacing.DOWN;
			if (isInventoryEmpty(iInventory, facing))
			{
				return false;
			}
			if (iInventory instanceof ISidedInventory)
			{
				ISidedInventory iSidedInventory = (ISidedInventory)iInventory;
				int[] slots = iSidedInventory.getSlotsForFace(facing);
				for (int i : slots)
				{
					if (pullItemFromSlot(hopper, iInventory, i, facing))
					{
						return true;
					}
				}
			}
			else
			{
				int size = iInventory.getSizeInventory();
				for (int k = 0; k < size; ++k)
				{
					if (pullItemFromSlot(hopper, iInventory, k, facing))
					{
						return true;
					}
				}
			}
		}
		else
		{
			for (EntityItem entityItem : getCaptureItems(hopper.getWorld(), hopper.getXPos(), hopper.getYPos(), hopper.getZPos()))
			{
				if (putDropInInventoryAllSlots(hopper, entityItem))
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Pulls from the specified slot in the inventoryIn and places in any available slot in the inventoryOut. Returns true if
	 * the entire stack was moved
	 */
	public static boolean pullItemFromSlot(IInventory inventoryOut, IInventory inventoryIn, int index, EnumFacing direction)
	{
		ItemStack stack = inventoryIn.getStackInSlot(index);
		if (stack != null && canExtractItemFromSlot(inventoryIn, stack, index, direction))
		{
			ItemStack stackCopy = stack.copy();
			ItemStack leftItems = putStackInInventoryAllSlots(inventoryOut, inventoryIn.decrStackSize(index, 1), (EnumFacing)null);
			if (leftItems == null || leftItems.stackSize == 0)
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
		boolean flag = false;
		if (itemIn == null)
		{
			return false;
		}
		else
		{
			ItemStack stack = itemIn.getEntityItem().copy();
			ItemStack leftItems = putStackInInventoryAllSlots(inventoryIn, stack, (EnumFacing)null);
			if (leftItems != null && leftItems.stackSize != 0)
			{
				itemIn.setEntityItemStack(leftItems);
			}
			else
			{
				flag = true;
				itemIn.setDead();
			}
			return flag;
		}
	}

	/**
	 * Attempts to place the passed stack in the inventory, using as many slots as required. Returns leftover items
	 */
	public static ItemStack putStackInInventoryAllSlots(IInventory inventoryIn, ItemStack stack, @Nullable EnumFacing side)
	{
		if (inventoryIn instanceof ISidedInventory && side != null)
		{
			ISidedInventory iSidedInventory = (ISidedInventory)inventoryIn;
			int[] slots = iSidedInventory.getSlotsForFace(side);
			for (int k = 0; k < slots.length && stack != null && stack.stackSize > 0; ++k)
			{
				stack = insertStack(inventoryIn, stack, slots[k], side);
			}
		}
		else
		{
			int size = inventoryIn.getSizeInventory();
			for (int j = 0; j < size && stack != null && stack.stackSize > 0; ++j)
			{
				stack = insertStack(inventoryIn, stack, j, side);
			}
		}
		
		if (stack != null && stack.stackSize == 0)
		{
			stack = null;
		}
		return stack;
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
	public static ItemStack insertStack(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side)
	{
		ItemStack stackInSlot = inventoryIn.getStackInSlot(index);
		if (canInsertItemInSlot(inventoryIn, stack, index, side))
		{
			boolean isHopper = false;
			if (stackInSlot == null)
			{
				int max = Math.min(stack.getMaxStackSize(), inventoryIn.getInventoryStackLimit());
				if (max >= stack.stackSize)
				{
					inventoryIn.setInventorySlotContents(index, stack);
					stack = null;
				}
				else
				{
					inventoryIn.setInventorySlotContents(index, stack.splitStack(max));
				}
				isHopper = true;
			}
			else if (canCombine(stackInSlot, stack))
			{
				int max = Math.min(stack.getMaxStackSize(), inventoryIn.getInventoryStackLimit());
				if (max > stackInSlot.stackSize)
				{
					int i = max - stackInSlot.stackSize;
					int j = Math.min(stack.stackSize, i);
					stack.stackSize -= j;
					stackInSlot.stackSize += j;
					isHopper = j > 0;
				}
			}
			
			if (isHopper)
			{
				if (inventoryIn instanceof TileEntityHopper)
				{
					TileEntityHopper tileEntityHopper = (TileEntityHopper)inventoryIn;
					if (tileEntityHopper.mayTransfer())
					{
						tileEntityHopper.setTransferCooldown(8);
					}
					inventoryIn.markDirty();
				}
				inventoryIn.markDirty();
			}
		}
		return stack;
	}

	/**
	 * Returns the IInventory for the specified hopper
	 */
	public static IInventory getHopperInventory(IHopper hopper)
	{
		return getInventoryAtPosition(hopper.getWorld(), hopper.getXPos(), hopper.getYPos() + 1.0D, hopper.getZPos());
	}

	public static List<EntityItem> getCaptureItems(World worldIn, double x, double y, double z)
	{
		return worldIn.<EntityItem>getEntitiesWithinAABB(EntityItem.class, 
				new AxisAlignedBB(x - 0.5D, y, z - 0.5D, x + 0.5D, y + 1.5D, z + 0.5D), 
				EntitySelectors.IS_ALIVE);
	}
    
	public static IInventory getInventoryAtPosition(World world, BlockPos pos)
	{
		return getInventoryAtPosition(world, pos.getX(), pos.getY(), pos.getZ());
	}

	/**
	 * Returns the IInventory (if applicable) of the TileEntity at the specified position
	 */
	public static IInventory getInventoryAtPosition(World worldIn, double x, double y, double z)
	{
		IInventory iInventory = null;
		int i = MathHelper.floor_double(x);
		int j = MathHelper.floor_double(y);
		int k = MathHelper.floor_double(z);
		BlockPos blockPos = new BlockPos(i, j, k);
		Block block = worldIn.getBlockState(blockPos).getBlock();
		if (block.hasTileEntity())
		{
			TileEntity tileEntity = worldIn.getTileEntity(blockPos);
			if (tileEntity instanceof IInventory)
			{
				iInventory = (IInventory)tileEntity;
				if (iInventory instanceof TileEntityChest && block instanceof BlockChest)
				{
					iInventory = ((BlockChest)block).getContainer(worldIn, blockPos, true);
				}
			}
		}
		if (iInventory == null)
		{
			List<Entity> list = worldIn.getEntitiesInAABBexcluding((Entity)null, 
					new AxisAlignedBB(x - 0.5D, y - 0.5D, z - 0.5D, x + 0.5D, y + 0.5D, z + 0.5D), 
					EntitySelectors.HAS_INVENTORY);
			if (!list.isEmpty())
			{
				iInventory = (IInventory)list.get(worldIn.rand.nextInt(list.size()));
			}
		}
		return iInventory;
	}

	public static boolean canCombine(ItemStack stack1, ItemStack stack2)
	{
		return stack1.getItem() != stack2.getItem() ? 
				false : 
					(stack1.getMetadata() != stack2.getMetadata() ? 
							false : 
								(stack1.stackSize > stack1.getMaxStackSize() ? 
										false : 
											ItemStack.areItemStackTagsEqual(stack1, stack2)));
	}
}