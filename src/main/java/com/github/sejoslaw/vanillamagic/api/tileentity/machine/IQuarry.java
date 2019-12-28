package com.github.sejoslaw.vanillamagic.api.tileentity.machine;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

/**
 * This is the connection to the TileQuarry.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface IQuarry extends IMachine {
    /**
     * @return Returns Quarry size in blocks.
     */
    int getQuarrySize();

    /**
     * @return Returns the Quarry mining direction. Facing from Cauldron to the
     * start mining position. <br>
     * <br>
     * rotateY(getStartPosFacing(), 1) - returns the direction of
     * {@link IInventory} on right from Cauldron<br>
     * rotateY(getStartPosFacing(), 2) - returns the direction of
     * RedstoneBlock<br>
     * rotateY(getStartPosFacing(), 3) - returns the direction of
     * DiamondBock
     * @see {@link #rotateY(Direction, int)}
     */
    Direction getStartPosDirection();

    /**
     * In short, this will restart the Quarry mining position.
     */
    void restartDefaultStartPos();

    /**
     * @return Returns facings around the Quarry;
     */
    Direction[] directions();

    /**
     * @return Returns TRUE if the Quarry can mine next block.
     */
    boolean canDig();

    /**
     * Spawns the given ItemStack.
     */
    void spawnDigged(ItemStack digged);

    /**
     * @return Moves the working position to the next position and returns it.
     */
    BlockPos moveWorkingPosToNextPos();

    /**
     * One Quarry operation. <br>
     * Check if Air Block OR check if Bedrock OR mine block, etc.
     */
    void performOneOperation();

    /**
     * @return Rotates the given facing the number of given times and returns this
     * facing after rotation. <br>
     * This will only rotate Horizontally.
     * @see {@link net.minecraft.util.Direction.Plane#HORIZONTAL}
     */
    Direction rotateY(Direction startFace, int times);

    /**
     * Forces Quarry to stop it's work. <br>
     * Quarry won't start until You use {@link #forceStart()}.
     */
    void forceStop();

    /**
     * Forces Quarry to start it's work.
     */
    void forceStart();

    /**
     * @return Returns QuarryUpgradeHelper connected with this Quarry. <br>
     * Each Quarry has it's own QuarryUpgradeHelper.
     */
    IQuarryUpgradeHelper getUpgradeHelper();
}