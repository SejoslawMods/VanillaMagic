package com.github.sejoslaw.vanillamagic.api.inventory;

import com.github.sejoslaw.vanillamagic.api.exception.NotInventoryException;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This interface is used to hold all the basic information about a single
 * {@link IInventory} on {@link World}.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface IInventoryWrapper {
    /**
     * Setting this wrapper for the new {@link IInventory} based on {@link World}
     * and it's {@link BlockPos}.
     *
     * @throws NotInventoryException thrown if the {@link TileEntity} at given
     *                               {@link BlockPos} can't be cast to
     *                               {@link IInventory}
     */
    void setNewInventory(World world, BlockPos position) throws NotInventoryException;

    /**
     * @return Returns the holding {@link IInventory}.
     */
    IInventory getInventory();

    /**
     * @return Returns the {@link World} with this {@link IInventory}.
     */
    World getWorld();

    /**
     * @return Returns the position of this {@link IInventory}.
     */
    BlockPos getPos();

    /**
     * @return Returns the current state of the {@link IInventory} block.
     */
    BlockState getBlockState();
}