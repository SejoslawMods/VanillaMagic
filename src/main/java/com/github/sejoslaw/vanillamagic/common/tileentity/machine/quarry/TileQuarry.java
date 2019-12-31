package com.github.sejoslaw.vanillamagic.common.tileentity.machine.quarry;

import com.github.sejoslaw.vanillamagic.api.event.EventQuarry;
import com.github.sejoslaw.vanillamagic.api.exception.NotInventoryException;
import com.github.sejoslaw.vanillamagic.api.inventory.InventoryWrapper;
import com.github.sejoslaw.vanillamagic.api.tileentity.machine.quarry.IQuarry;
import com.github.sejoslaw.vanillamagic.api.tileentity.machine.quarry.IQuarryUpgradeManager;
import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.inventory.InventoryHelper;
import com.github.sejoslaw.vanillamagic.common.tileentity.machine.TileMachine;
import com.github.sejoslaw.vanillamagic.common.util.BlockPosUtil;
import com.github.sejoslaw.vanillamagic.common.util.BlockUtil;
import com.github.sejoslaw.vanillamagic.common.util.EventUtil;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import com.github.sejoslaw.vanillamagic.core.VMLogger;
import com.github.sejoslaw.vanillamagic.core.VMTileEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import org.apache.logging.log4j.Level;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class TileQuarry extends TileMachine implements IQuarry {
    public static final String REGISTRY_NAME = TileQuarry.class.getName();

    private IQuarryUpgradeManager upgradeManager = new QuarryUpgradeManager(this);

    private BlockPos diamondBlockPos;
    private BlockPos redstoneBlockPos;

    /**
     * It is not final, but should never be changed. <br>
     * It is set in the checkSurroundings().
     */
    private Direction startPosFacing;
    private Direction diamondFacing;
    private Direction redstoneFacing;

    /**
     * Forces Quarry to stop working.
     */
    private boolean forceStop = false;

    /**
     * It's (size)x(size) but (size-2)x(size-2) is for digging BlocksInChunk Only
     * multiply this
     */
    private int quarrySize = QuarrySizeHelper.BASIC_SIZE;
    private int redstoneBlocks = 0;

    public TileQuarry() {
        super(VMTileEntities.QUARRY);
    }

    public int getQuarrySize() {
        return this.quarrySize;
    }

    public Direction getStartPosDirection() {
        return this.startPosFacing;
    }

    public void forceStop() {
        this.forceStop = true;
    }

    public void forceStart() {
        this.forceStop = false;
    }

    public IQuarryUpgradeManager getUpgradeManager() {
        return this.upgradeManager;
    }

    /**
     * Method for checking DiamondBlock and RedstoneBlock
     */
    public boolean checkSurroundings() {
        if (((this.diamondBlockPos != null) && !BlockUtil.areEqual(Blocks.DIAMOND_BLOCK, world.getBlockState(this.diamondBlockPos).getBlock()))
                || ((this.redstoneBlockPos != null) && !BlockUtil.areEqual(Blocks.REDSTONE_BLOCK, world.getBlockState(this.redstoneBlockPos).getBlock()))) {
            return false;
        }

        if (this.redstoneBlockPos != null && this.diamondBlockPos != null) {
            return true;
        }

        for (Direction redstoneFacing : directions()) {
            this.redstoneBlockPos = pos.offset(redstoneFacing);

            if (BlockUtil.areEqual(world.getBlockState(this.redstoneBlockPos).getBlock(), Blocks.REDSTONE_BLOCK)) {
                Direction diamondFacing = redstoneFacing.rotateY();
                this.diamondBlockPos = pos.offset(diamondFacing);

                if (BlockUtil.areEqual(world.getBlockState(this.diamondBlockPos).getBlock(), Blocks.DIAMOND_BLOCK)) {
                    this.redstoneFacing = redstoneFacing;
                    this.diamondFacing = diamondFacing;

                    this.startPosFacing = this.diamondFacing.rotateY();
                    this.restartDefaultStartPos();

                    this.chestPosInput = new BlockPos(this.pos.getX(), this.pos.getY() + 1, this.pos.getZ());
                    this.chestPosOutput = pos.offset(this.getOutputDirection());

                    try {
                        this.inventoryInput = new InventoryWrapper(world, this.chestPosInput);
                        this.inventoryOutput = new InventoryWrapper(world, this.chestPosOutput);

                        EventUtil.postEvent(new EventQuarry.BuildCorrectly(this, world, pos));
                    } catch (NotInventoryException e) {
                        VMLogger.log(Level.ERROR, this.getClass().getSimpleName() + " - error when converting to IInventory at position: " + e.position.toString());
                    }

                    return true;
                }
            }
        }

        return false;
    }

    public void restartDefaultStartPos() {
        startPos = pos.offset(this.startPosFacing);
        workingPos = pos.offset(this.startPosFacing);
    }

    public Iterable<Direction> directions() {
        return Direction.Plane.HORIZONTAL;
    }

    public boolean canDig() {
        return ticks >= oneOperationCost;
    }

    public void spawnAfterDig(ItemStack digStack) {
        Block.spawnAsEntity(world, this.pos.offset(Direction.UP, 2), digStack);
    }

    public BlockPos moveWorkingPosToNextPos() {
        return workingPos.offset(Direction.DOWN);
    }

    /**
     * Here I want to check for the number of IQuarryUpgrades next to the Quarry.
     * <br>
     * Count them and change the Quarry size.
     */
    protected void performAdditionalOperations() {
        int diamondBlocks = 0;
        Block checkingBlock;

        BlockPos cauldronPos = new BlockPos(this.pos);
        cauldronPos = cauldronPos.offset(this.diamondFacing);
        BlockState checkingState = world.getBlockState(cauldronPos);

        while (true) {
            checkingBlock = checkingState.getBlock();

            if (BlockUtil.areEqual(checkingBlock, Blocks.DIAMOND_BLOCK)) {
                diamondBlocks++;
            } else if (QuarryUpgradeRegistry.isUpgradeBlock(checkingBlock)) {
                this.upgradeManager.addUpgradeFromBlock(checkingBlock, cauldronPos);
            } else {
                break;
            }

            cauldronPos = cauldronPos.offset(this.diamondFacing);
            checkingState = world.getBlockState(cauldronPos);
        }

        this.upgradeManager.modifyQuarry(this);
        this.quarrySize = QuarrySizeHelper.calculateSize(diamondBlocks);
    }

    public List<ITextComponent> getAdditionalInfo() {
        List<ITextComponent> infos = super.getAdditionalInfo();
        infos.add(TextUtil.wrap("Quarry Size: " + this.quarrySize + " blocks"));

        this.performAdditionalOperations();

        this.upgradeManager.addAdditionalInfo(infos);

        this.upgradeManager.clearUpgrades();
        this.countRedstoneBlocks();

        infos.add(TextUtil.wrap("Mining blocks per tick: " + this.redstoneBlocks));

        return infos;
    }

    public void doWork() {
        if (this.forceStop) {
            return;
        }

        this.countRedstoneBlocks();

        for (int i = 0; i < this.redstoneBlocks; ++i) {
            performOneOperation();
        }

        EventUtil.postEvent(new EventQuarry.Work(this, world, pos));
        this.upgradeManager.clearUpgrades();
    }

    public void countRedstoneBlocks() {
        BlockPos cauldronPos = new BlockPos(this.pos);
        cauldronPos = cauldronPos.offset(this.redstoneFacing);

        this.redstoneBlocks = 0;
        BlockState checkingBlock = world.getBlockState(cauldronPos);

        while (BlockUtil.areEqual(checkingBlock.getBlock(), Blocks.REDSTONE_BLOCK)) {
            this.redstoneBlocks++;

            cauldronPos = cauldronPos.offset(this.redstoneFacing);
            checkingBlock = world.getBlockState(cauldronPos);
        }
    }

    public void performOneOperation() {
        if (!canDig()) {
            return;
        }

        if (world.isAirBlock(workingPos)) {
            while (!world.isAirBlock(workingPos)) {
                workingPos = this.moveWorkingPosToNextPos();
            }
        } else if (BlockUtil.areEqual(world.getBlockState(workingPos).getBlock(), Blocks.BEDROCK)) {
            this.goToNextPosAfterHitBedrock();
        } else {
            boolean hasChest = this.getOutputInventory() != null;
            BlockState stateToDig = world.getBlockState(workingPos);
            Block blockToDig = stateToDig.getBlock();
            List<ItemStack> drops = this.upgradeManager.getDrops(blockToDig, world, workingPos, stateToDig);

            if (blockToDig instanceof IInventory) {
                IInventory inv = (IInventory) blockToDig;

                for (int i = 0; i < inv.getSizeInventory(); ++i) {
                    ItemStack stack = inv.getStackInSlot(i);

                    if (!ItemStackUtil.isNullStack(stack)) {
                        drops.add(stack);
                    }
                }
            }

            for (ItemStack dropStack : drops) {
                if (!hasChest) {
                    this.spawnAfterDig(dropStack);
                } else if (hasChest) {
                    ItemStack leftItems = InventoryHelper.putStackInInventoryAllSlots(getOutputInventory().getInventory(), dropStack, getOutputDirection());

                    if (ItemStackUtil.isNullStack(leftItems)) {
                        break;
                    }

                    if (ItemStackUtil.getStackSize(leftItems) > 0) {
                        this.spawnAfterDig(leftItems);
                    }
                }
            }

            world.setBlockState(workingPos, Blocks.AIR.getDefaultState());
        }

        workingPos = this.moveWorkingPosToNextPos();
    }

    /**
     * Rotates the given facing the number of given times and returns this facing
     * after rotation. <br>
     * This will only rotate Horizontally.
     *
     * @see {@link net.minecraft.util.Direction.Plane#HORIZONTAL}
     */
    public Direction rotateY(Direction startFace, int times) {
        for (int i = 0; i < times; ++i) {
            startFace = startFace.rotateY();
        }

        return startFace;
    }

    public void goToNextPosAfterHitBedrock() {
        workingPos = new BlockPos(workingPos.getX(), startPos.getY(), workingPos.getZ()).offset(this.startPosFacing);

        if (BlockPosUtil.distanceInLine(workingPos, startPos) > this.quarrySize) {
            startPos = startPos.offset(rotateY(this.startPosFacing, 3));
            workingPos = new BlockPos(startPos);
        }
    }

    public Direction getOutputDirection() {
        return this.startPosFacing.rotateY();
    }

    public boolean isPrepared() {
        return this.checkSurroundings();
    }
}