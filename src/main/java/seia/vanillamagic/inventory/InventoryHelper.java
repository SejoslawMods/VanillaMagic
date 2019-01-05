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
import seia.vanillamagic.util.ItemStackUtil;

/**
 * Class which add various methods to operate on MC inventory.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class InventoryHelper {
	/**
	 * TileEntityHopper.class
	 */
	private static Class<?> CLASS_TILE_HOPPER = null;

	private InventoryHelper() {
	}

	static {
		try {
			CLASS_TILE_HOPPER = Class.forName("net.minecraft.tileentity.TileEntityHopper");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return Returns TRUE if there is an inventory on the specif ied position.
	 */
	public static boolean isInventory(World world, BlockPos pos) {
		return world.getTileEntity(pos) instanceof IInventory;
	}

	/**
	 * Drop all items from specif ied inventory.
	 */
	public static void dropInventoryItems(World worldIn, BlockPos pos, IInventory inventory) {
		net.minecraft.inventory.InventoryHelper.dropInventoryItems(worldIn, pos, inventory);
	}

	/**
	 * Drop all items from specif ied inventory.
	 */
	public static void dropInventoryItems(World worldIn, Entity entityAt, IInventory inventory) {
		net.minecraft.inventory.InventoryHelper.dropInventoryItems(worldIn, entityAt, inventory);
	}

	/**
	 * Spawn specif ied ItemStack on specif ied position.
	 */
	public static void spawnItemStack(World worldIn, BlockPos pos, ItemStack stack) {
		net.minecraft.inventory.InventoryHelper.spawnItemStack(worldIn, pos.getX(), pos.getY(), pos.getZ(), stack);
	}

	/**
	 * @return Returns new ItemHandler for given inventory.
	 */
	public static IItemHandler createUnSidedHandler(IInventory inv) {
		return new InvWrapper(inv);
	}

	/**
	 * @return Returns false if the inventory has any room to place items in
	 */
	public static boolean isInventoryFull(IInventory inventoryIn, EnumFacing side) {
		if (inventoryIn instanceof ISidedInventory) {
			ISidedInventory isidedinventory = (ISidedInventory) inventoryIn;
			int[] slots = isidedinventory.getSlotsForFace(side);

			for (int slot : slots) {
				ItemStack stack = isidedinventory.getStackInSlot(slot);
				if (ItemStackUtil.isNullStack(stack) || ItemStackUtil.getStackSize(stack) != stack.getMaxStackSize()) {
					return false;
				}
			}
		} else {
			int slots = inventoryIn.getSizeInventory();

			for (int slot = 0; slot < slots; ++slot) {
				ItemStack stack = inventoryIn.getStackInSlot(slot);
				if (ItemStackUtil.isNullStack(stack) || ItemStackUtil.getStackSize(stack) != stack.getMaxStackSize()) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * @return Returns true if the given inventory has free slot.
	 */
	public static boolean hasInventoryFreeSpace(IInventory inventoryIn, EnumFacing side) {
		if (inventoryIn instanceof ISidedInventory) {
			ISidedInventory iSidedInventory = (ISidedInventory) inventoryIn;
			int[] slots = iSidedInventory.getSlotsForFace(side);

			for (int slot : slots) {
				ItemStack stack = iSidedInventory.getStackInSlot(slot);
				if (ItemStackUtil.isNullStack(stack)) {
					return true;
				}
			}
		} else {
			int size = inventoryIn.getSizeInventory();

			for (int j = 0; j < size; ++j) {
				ItemStack stack = inventoryIn.getStackInSlot(j);
				if (ItemStackUtil.isNullStack(stack)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @return Returns false if the specif ied IInventory contains any items
	 */
	public static boolean isInventoryEmpty(IInventory inventoryIn, EnumFacing side)
			throws ReflectiveOperationException {
		Method method = CLASS_TILE_HOPPER.getMethod("isInventoryEmpty", IInventory.class, EnumFacing.class);
		method.setAccessible(true);
		return (boolean) method.invoke(null, inventoryIn, side);
	}

	/**
	 * @return Returns TRUE if the Hopper captured items.
	 */
	public static boolean captureDroppedItems(IHopper hopper) {
		return TileEntityHopper.pullItems(hopper);
	}

	/**
	 * Pulls from the specif ied slot in the inventoryIn and places in any available
	 * slot in the inventoryOut.
	 * 
	 * @return Returns true if the entire stack was moved.
	 */
	public static boolean pullItemFromSlot(IInventory inventoryOut, IInventory inventoryIn, int index,
			EnumFacing direction) {
		ItemStack stack = inventoryIn.getStackInSlot(index);

		if (!ItemStackUtil.isNullStack(stack) && canExtractItemFromSlot(inventoryIn, stack, index, direction)) {
			ItemStack stackCopy = stack.copy();
			ItemStack leftItems = putStackInInventoryAllSlots(inventoryOut, inventoryIn.decrStackSize(index, 1),
					(EnumFacing) null);

			if (ItemStackUtil.isNullStack(leftItems) || ItemStackUtil.getStackSize(leftItems) == 0) {
				inventoryIn.markDirty();
				return true;
			}

			inventoryIn.setInventorySlotContents(index, stackCopy);
		}
		return false;
	}

	/**
	 * Attempts to place the passed EntityItem's stack into the inventory using as
	 * many slots as possible.
	 * 
	 * @return Returns false if the stackSize of the drop was not depleted.
	 */
	public static boolean putDropInInventoryAllSlots(IInventory inventoryIn, EntityItem itemIn) {
		return TileEntityHopper.putDropInInventoryAllSlots(null, inventoryIn, itemIn);
	}

	/**
	 * Attempts to place the passed stack in the inventory, using as many slots as
	 * required.
	 * 
	 * @return Returns leftover items.
	 */
	@Nullable
	public static ItemStack putStackInInventoryAllSlots(IInventory inventoryIn, ItemStack stack,
			@Nullable EnumFacing side) {
		return TileEntityHopper.putStackInInventoryAllSlots(null, inventoryIn, stack, side);
	}

	/**
	 * Can insert the specif ied item from the specif ied slot on the specif ied
	 * side?
	 */
	public static boolean canInsertItemInSlot(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side) {
		return !inventoryIn.isItemValidForSlot(index, stack) ? false
				: !(inventoryIn instanceof ISidedInventory)
						|| ((ISidedInventory) inventoryIn).canInsertItem(index, stack, side);
	}

	/**
	 * Can extract the specif ied item from the specif ied slot on the specif ied
	 * side?
	 */
	public static boolean canExtractItemFromSlot(IInventory inventoryIn, ItemStack stack, int index, EnumFacing side) {
		return !(inventoryIn instanceof ISidedInventory)
				|| ((ISidedInventory) inventoryIn).canExtractItem(index, stack, side);
	}

	/**
	 * Insert the specif ied stack to the specif ied inventory.
	 * 
	 * @return Returns any leftover items
	 */
	@Nullable
	public static ItemStack insertStack(IInventory inventoryIn, IInventory stack, ItemStack index, int side,
			EnumFacing facing) throws ReflectiveOperationException {
		Method method = CLASS_TILE_HOPPER.getMethod("insertStack", IInventory.class, IInventory.class, ItemStack.class,
				int.class, EnumFacing.class);
		method.setAccessible(true);
		return (ItemStack) method.invoke(null, inventoryIn, stack, index, side, facing);
	}

	/**
	 * @return Returns the IInventory for the specif ied hopper.
	 */
	public static IInventory getHopperInventory(IHopper hopper) {
		return getInventoryAtPosition(hopper.getWorld(), hopper.getXPos(), hopper.getYPos() + 1.0D, hopper.getZPos());
	}

	/**
	 * @return Returns list with all captured items at specif ied position.
	 */
	public static List<EntityItem> getCaptureItems(World worldIn, double x, double y, double z) {
		return TileEntityHopper.getCaptureItems(worldIn, x, y, z);
	}

	/**
	 * @return Returns an inventory from specif ied position.
	 */
	@Nullable
	public static IInventory getInventoryAtPosition(World world, BlockPos pos) {
		return getInventoryAtPosition(world, pos.getX(), pos.getY(), pos.getZ());
	}

	/**
	 * @return Returns the IInventory (if applicable) of the TileEntity at the
	 *         specif ied position.
	 */
	@Nullable
	public static IInventory getInventoryAtPosition(World worldIn, double x, double y, double z) {
		return TileEntityHopper.getInventoryAtPosition(worldIn, x, y, z);
	}

	/**
	 * @return Returns TRUE if the two stacks can combine.
	 */
	public static boolean canCombine(ItemStack stack1, ItemStack stack2) throws ReflectiveOperationException {
		Method method = CLASS_TILE_HOPPER.getMethod("canCombine", ItemStack.class, ItemStack.class);
		method.setAccessible(true);
		return (boolean) method.invoke(null, stack1, stack2);
	}

	/**
	 * @return Returns the first slot from inventory which is not a Null Slot.
	 */
	public static int getFirstNotNull(IInventory inv) {
		for (int i = 0; i < inv.getSizeInventory(); ++i)
			if (!ItemStackUtil.isNullStack(inv.getStackInSlot(i)))
				return i;
		return -1;
	}

	/**
	 * @return Returns the slot number of the first found inventory.<br>
	 *         if there is no inventory block, will return -1
	 */
	public static int containsAnotherInventoryBlock(IInventoryWrapper wrapper) {
		IInventory inv = wrapper.getInventory();

		for (int i = 0; i < inv.getSizeInventory(); ++i) {
			ItemStack checkingStack = inv.getStackInSlot(i);

			if (!ItemStackUtil.isNullStack(checkingStack)) {
				Item checkingItem = checkingStack.getItem();
				Block blockFromItem = Block.getBlockFromItem(checkingItem);

				if (blockFromItem != null) {
					IBlockState blockFromItemState = blockFromItem.getDefaultState();

					if (blockFromItem.hasTileEntity(blockFromItemState)) {
						TileEntity tileFromBlock = blockFromItem.createTileEntity(wrapper.getWorld(),
								blockFromItemState);

						if (tileFromBlock instanceof IInventory) {
							return i;
						}
					}
				}
			}
		}
		return -1;
	}
}