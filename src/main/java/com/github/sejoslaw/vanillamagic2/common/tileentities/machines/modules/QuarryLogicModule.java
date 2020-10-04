package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import com.github.sejoslaw.vanillamagic2.common.utils.BlockUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuarryLogicModule extends AbstractLogicModule {
    private static final String NBT_MODULE_QUARRY_START_POS_DIRECTION_ID = "NBT_MODULE_QUARRY_START_POS_DIRECTION_ID";
    private static final String NBT_MODULE_QUARRY_DIAMOND_POS = "NBT_MODULE_QUARRY_DIAMOND_POS";
    private static final String NBT_MODULE_QUARRY_REDSTONE_POS = "NBT_MODULE_QUARRY_REDSTONE_POS";

    public void setup(IVMTileMachine machine) {
        BlockPos machinePos = machine.getPos();
        Direction diamondDir = Direction.UP;
        BlockPos diamondPos = BlockPos.ZERO;

        for (Direction face : Direction.Plane.HORIZONTAL) {
            diamondPos = machinePos.offset(face);

            if (machine.getWorld().getBlockState(diamondPos).getBlock() == Blocks.DIAMOND_BLOCK) {
                diamondDir = face;
                break;
            }
        }

        this.setPos(machine, diamondPos, NBT_MODULE_QUARRY_DIAMOND_POS);
        this.setPos(machine, machinePos.offset(diamondDir.rotateYCCW()), NBT_MODULE_QUARRY_REDSTONE_POS);
        this.setInt(machine, diamondDir.rotateY().getIndex(), NBT_MODULE_QUARRY_START_POS_DIRECTION_ID);

        this.setOutputStoragePos(machine, machinePos.offset(diamondDir.getOpposite()));
        this.setInputStoragePos(machine, machinePos.offset(Direction.UP));
        this.setEnergySourcePos(machine, machinePos.offset(Direction.UP));

        this.setStartPos(machine, machinePos.offset(diamondDir.rotateY()));
        this.setWorkingPos(machine, machinePos.offset(diamondDir.rotateY()));
    }

    protected boolean checkStructure(IVMTileMachine machine) {
        return this.hasKey(machine, NBT_MODULE_QUARRY_START_POS_DIRECTION_ID);
    }

    protected void work(IVMTileMachine machine) {
        int quarrySize = this.countBlocks(machine, this.getDirection(machine, NBT_MODULE_QUARRY_START_POS_DIRECTION_ID).rotateYCCW(), Blocks.DIAMOND_BLOCK) * VMForgeConfig.QUARRY_SIZE.get();

        for (int i = 0 ; i < this.countBlocks(machine, this.getDirection(machine, NBT_MODULE_QUARRY_START_POS_DIRECTION_ID).getOpposite(), Blocks.REDSTONE_BLOCK); ++i) {
            this.performSingleOperation(machine, quarrySize);
        }
    }

    private int countBlocks(IVMTileMachine machine, Direction dir, Block block) {
        BlockPos machinePos = machine.getPos().offset(dir);
        int blocksCount = 0;

        while (machine.getWorld().getBlockState(machinePos).getBlock().equals(block)) {
            blocksCount++;
            machinePos = machinePos.offset(dir);
        }

        return blocksCount;
    }

    /**
     * Performs single Quarry mining / moving operation
     */
    private void performSingleOperation(IVMTileMachine machine, int quarrySize) {
        IWorld world = machine.getWorld();
        BlockPos workingPos = this.getWorkingPos(machine);
        BlockPos startPos = this.getStartPos(machine);
        Direction startPosDir = this.getDirection(machine, NBT_MODULE_QUARRY_START_POS_DIRECTION_ID);
        BlockState mineBlockState = world.getBlockState(workingPos);

        if (this.canSkipBlock(world, workingPos)) {
            while (this.canSkipBlock(world, workingPos)) {
                workingPos = workingPos.offset(Direction.DOWN);
            }

            this.setWorkingPos(machine, workingPos);
            this.setStartPos(machine, startPos);
            this.performSingleOperation(machine, quarrySize);
        } else if (mineBlockState.getBlock() == Blocks.BEDROCK) {
            workingPos = new BlockPos(workingPos.getX(), startPos.getY(), workingPos.getZ()).offset(startPosDir);

            if (BlockUtils.distanceInLine(workingPos, startPos) > quarrySize) {
                startPos = startPos.offset(startPosDir.rotateYCCW());
                workingPos = new BlockPos(startPos);
            }

            this.setWorkingPos(machine, workingPos);
            this.setStartPos(machine, startPos);
            this.performSingleOperation(machine, quarrySize);
        } else {
            List<ItemStack> drops = Block.getDrops(mineBlockState, (ServerWorld) world, workingPos, machine.getTileEntity());
            IInventory mineInv = WorldUtils.getInventory(world, workingPos);

            if (mineInv != null) {
                for (int i = 0; i < mineInv.getSizeInventory(); ++i) {
                    ItemStack stack = mineInv.getStackInSlot(i);

                    if (stack != ItemStack.EMPTY) {
                        drops.add(stack);
                    }
                }
            }

            BlockPos outputInvPos = this.getOutputStoragePos(machine);
            IInventory outputInv = WorldUtils.getInventory(world, outputInvPos);

            drops.forEach(stack -> {
                if (outputInv == null) {
                    Block.spawnAsEntity(WorldUtils.asWorld(world), machine.getPos().offset(Direction.UP, 2), stack);
                } else {
                    ItemStack leftStack = HopperTileEntity.putStackInInventoryAllSlots(null, outputInv, stack, startPosDir.rotateY());

                    if (leftStack != ItemStack.EMPTY && leftStack.getCount() > 0) {
                        Block.spawnAsEntity(WorldUtils.asWorld(world), machine.getPos().offset(Direction.UP, 2), leftStack);
                    }
                }
            });

            world.setBlockState(workingPos, Blocks.AIR.getDefaultState(), 1 | 2);
            workingPos = workingPos.offset(Direction.DOWN);

            this.setWorkingPos(machine, workingPos);
            this.setStartPos(machine, startPos);
        }
    }

    private boolean canSkipBlock(IWorld world, BlockPos pos) {
        return world.isAirBlock(pos) || world.getFluidState(pos) != Fluids.EMPTY.getDefaultState();
    }
}
