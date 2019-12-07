package com.github.sejoslaw.vanillamagic.api.tileentity.machine;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import com.github.sejoslaw.vanillamagic.api.inventory.IInventoryWrapper;
import com.github.sejoslaw.vanillamagic.api.tileentity.ICustomTileEntity;

/**
 * Machine is a TileEntity that perform some work on World. This interface is
 * made to unify some of the Machine work.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface IMachine extends ICustomTileEntity {
    /**
     * @return Returns the actual position of the machine block. By MachinePosition
     * we understand the core of the Machine. In Vanilla Magic it usually
     * will return Cauldron position.
     */
    BlockPos getMachinePos();

    /**
     * @return Returns the state of this Machine.
     */
    BlockState getMachineState();

    /**
     * @return Returns the Machine block.
     */
    Block getMachineBlock();

    /**
     * Set Machine position.
     */
    void setMachinePos(BlockPos newPos);

    /**
     * @return Returns the current Machine working position. In Quarry it's a
     * digging position. It should return the position on which Machine is
     * performing work.
     */
    BlockPos getWorkingPos();

    /**
     * Set the position on which Machine will work. It can be make directly in
     * 'onUpdate' method.
     */
    void setWorkingPos(BlockPos newPos);

    /**
     * @return Returns the starting position at which the Machine will start to
     * operate.
     */
    BlockPos getStartPos();

    /**
     * Set new Machine starting position. After this being set, the Machine should
     * restart the work.
     */
    void setNewStartPos(BlockPos newStartPos);

    /**
     * @return Returns the Machine work radius (in blocks).
     */
    int getWorkRadius();

    /**
     * Sets the new radius for this Machine.
     */
    void setWorkRadius(int newRadius);

    /**
     * @return Returns the cost of performing one operation.
     */
    int getOneOperationCost();

    /**
     * Each Machine uses ticks. Ticks works like Furnace smelting ticks. For
     * instance Coal as fuel will return 1600 ticks.
     *
     * @return Returns the current ticks in machine. Dividing this by
     * getOneOperationCost() will give You the amount of operations the
     * Machine can handle.
     */
    int getCurrentTicks();

    /**
     * Each Machine uses ticks. Ticks works like Furnace smelting ticks. For
     * instance Coal as fuel will return 1600 ticks. It will set the current ticks
     * to the given value.
     */
    void setCurrentTicks(int ticks);

    /**
     * @return Returns the max number of ticks that Machine can contain. It's
     * efficient to make it: 10 * getOneOperationCost(). If that made, the
     * Machine won't use the infinite amount of resources.
     */
    int getMaxTicks();

    /**
     * Method to force the Machine for fuel-check.
     */
    void checkFuel();

    /**
     * Method to force the Machine to end it's work.
     */
    void endWork();

    /**
     * @return Returns if a Machine has finished work.
     */
    boolean finishedWork();

    /**
     * @return Returns what Player should hold in left hand (off hand) to activate
     * this Machine. This usually should be set in Quest.
     */
    @Nullable
    ItemStack getActivationStackLeftHand();

    /**
     * Set the activation stack that should be in left hand (off hand).
     */
    void setActivationStackLeftHand(ItemStack stack);

    /**
     * @return Returns what Player should hold in right hand (main hand) to activate
     * this Machine. This usually should be set in Quest.
     */
    @Nullable
    ItemStack getActivationStackRightHand();

    /**
     * Set the activation stack that should be in right hand (main hand).
     */
    void setActivationStackRightHand(ItemStack stack);

    /**
     * Simple method to detect if the Machine is active.
     */
    boolean isActive();

    /**
     * @return Returns the inventory from which Machine can take resources to work
     * (fuel, etc.).
     */
    @Nullable
    IInventoryWrapper getInputInventory();

    /**
     * Sets the new input inventory.
     */
    void setInputInventory(IInventoryWrapper inv);

    /**
     * @return Returns the inventory to which Machine should output.
     */
    @Nullable
    IInventoryWrapper getOutputInventory();

    /**
     * Sets the new output inventory.
     */
    void setOutputInventory(IInventoryWrapper inv);

    /**
     * @return Returns if the output inventory has space for more items.
     */
    Direction getOutputDirection();

    // =========================== NEIGHBOR ===========================

    /**
     * @return Returns the neighbor TileEntity at given face.
     */
    @Nullable
    TileEntity getNeighborTile(Direction face);

    /**
     * @return Returns the Machine that is next to this Machine.
     */
    @Nullable
    IMachine getNeighborMachine(Direction face);

    /**
     * @return Returns the neighbor position.
     */
    BlockPos getNeighborPos(Direction face);

    /**
     * @return Returns the state of the neighbor block.
     */
    BlockState getNeighborState(Direction face);

    /**
     * @return Returns the neighbor block.
     */
    Block getNeighborBlock(Direction face);
}