package com.github.sejoslaw.vanillamagic.common.inventory;

import com.github.sejoslaw.vanillamagic.api.inventory.IInventoryWrapper;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.lang.reflect.Method;

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
            CLASS_TILE_HOPPER = Class.forName("net.minecraft.tileentity.HopperTileEntity");
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
     * @return Returns false if the inventory has any room to place items in
     */
    public static boolean isInventoryFull(IInventory inventoryIn, Direction side) {
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
    public static boolean hasInventoryFreeSpace(IInventory inventoryIn, Direction side) {
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
    public static boolean isInventoryEmpty(IInventory inventoryIn, Direction side) throws ReflectiveOperationException {
        Method method = CLASS_TILE_HOPPER.getMethod("isInventoryEmpty", IInventory.class, Direction.class);
        method.setAccessible(true);
        return (boolean) method.invoke(null, inventoryIn, side);
    }

    /**
     * Attempts to place the passed stack in the inventory, using as many slots as
     * required.
     *
     * @return Returns leftover items.
     */
    @Nullable
    public static ItemStack putStackInInventoryAllSlots(IInventory inventoryIn, ItemStack stack, @Nullable Direction side) {
        return HopperTileEntity.putStackInInventoryAllSlots(null, inventoryIn, stack, side);
    }

    /**
     * Insert the specif ied stack to the specif ied inventory.
     *
     * @return Returns any leftover items
     */
    @Nullable
    public static ItemStack insertStack(IInventory inventoryIn, IInventory stack, ItemStack index, int side, Direction facing) throws ReflectiveOperationException {
        Method method = CLASS_TILE_HOPPER.getMethod("insertStack", IInventory.class, IInventory.class, ItemStack.class, int.class, Direction.class);
        method.setAccessible(true);
        return (ItemStack) method.invoke(null, inventoryIn, stack, index, side, facing);
    }

    /**
     * @return Returns the slot number of the first found inventory.<br>
     * if there is no inventory block, will return -1
     */
    public static int containsAnotherInventoryBlock(IInventoryWrapper wrapper) {
        IInventory inv = wrapper.getInventory();

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack checkingStack = inv.getStackInSlot(i);

            if (!ItemStackUtil.isNullStack(checkingStack)) {
                Item checkingItem = checkingStack.getItem();
                Block blockFromItem = Block.getBlockFromItem(checkingItem);

                BlockState blockFromItemState = blockFromItem.getDefaultState();

                if (blockFromItem.hasTileEntity(blockFromItemState)) {
                    TileEntity tileFromBlock = blockFromItem.createTileEntity(blockFromItemState, wrapper.getWorld());

                    if (tileFromBlock instanceof IInventory) {
                        return i;
                    }
                }
            }
        }

        return -1;
    }
}
