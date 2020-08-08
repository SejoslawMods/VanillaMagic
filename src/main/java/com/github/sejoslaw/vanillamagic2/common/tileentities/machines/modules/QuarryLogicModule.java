package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import com.github.sejoslaw.vanillamagic2.common.utils.BlockUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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

        for (Direction face : Direction.values()) {
            diamondPos = machinePos.offset(diamondDir);

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
        // TODO: Implement Quarry operation (in single tick)
        //  2.2. Is air block - if so then go to next 'mineable' block
        //  2.3. Else if Is Bedrock - go to next column (forward or one left)
        //  2.4. Else (mine block)
        //      2.4.1. Get drops from mined block
        //      2.4.2. If block is an inventory - get content
        //      2.4.3. Try to input into the input chest or spawn as ItemEntity
        //      2.4.4. Set workingPos to AIR
        //      2.4.5. Go to next 'mineable' block

        for(int i = 0 ; i < this.getRedstoneBlocksCount(machine); ++i) {
            this.performSingleOperation(machine);
        }
    }

    /**
     * Performs single Quarry mining operation
     */
    private void performSingleOperation(IVMTileMachine machine) {
        World world = machine.getWorld();
        BlockPos workingPos = this.getWorkingPos(machine);
        BlockPos startPos = this.getStartPos(machine);
        Direction startPosDir = this.getDirection(machine, NBT_MODULE_QUARRY_START_POS_DIRECTION_ID);
        BlockState mineBlockState = world.getBlockState(workingPos);

        if (world.isAirBlock(workingPos)) { // Go down
            while (!world.isAirBlock(workingPos)) {
                workingPos = workingPos.offset(Direction.DOWN);
            }
        } else if (mineBlockState.getBlock() == Blocks.BEDROCK) { // Nest Line
            workingPos = new BlockPos(workingPos.getX(), startPos.getY(), workingPos.getZ()).offset(startPosDir);

            if (BlockUtils.distanceInLine(workingPos, startPos) > this.quarrySize) { // TODO: Calculate Quarry size
                startPos = startPos.offset(startPosDir.rotateYCCW());
                workingPos = new BlockPos(startPos);
            }
        } else { // Mine
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
                    Block.spawnAsEntity(world, machine.getPos().offset(Direction.UP, 2), stack);
                }
            });
        }
    }

    private int getRedstoneBlocksCount(IVMTileMachine machine) {
        Direction redstoneDir = this.getDirection(machine, NBT_MODULE_QUARRY_START_POS_DIRECTION_ID).getOpposite();
        BlockPos machinePos = machine.getPos().offset(redstoneDir);
        int redstoneBlocksCount = 0;

        while (machine.getWorld().getBlockState(machinePos).getBlock().equals(Blocks.REDSTONE_BLOCK)) {
            redstoneBlocksCount++;
            machinePos = machinePos.offset(redstoneDir);
        }

        return redstoneBlocksCount;
    }
}
