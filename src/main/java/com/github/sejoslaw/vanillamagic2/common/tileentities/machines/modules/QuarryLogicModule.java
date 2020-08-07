package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuarryLogicModule extends AbstractLogicModule {
    private static final String NBT_MODULE_QUARRY_START_POS_DIRECTION_ID = "NBT_MODULE_QUARRY_START_POS_DIRECTION_ID";
    private static final String NBT_MODULE_QUARRY_DIAMOND_POS = "NBT_MODULE_QUARRY_DIAMOND_POS";
    private static final String NBT_MODULE_QUARRY_REDSTONE_POS = "NBT_MODULE_QUARRY_REDSTONE_POS";

    public void setup(IVMTileMachine machine) {
        BlockPos machinePos = machine.getPos();

        for (Direction diamondDir : Direction.values()) {
            BlockPos diamondPos = machinePos.offset(diamondDir);

            if (machine.getWorld().getBlockState(diamondPos).getBlock() == Blocks.DIAMOND_BLOCK) {
                this.setPos(machine, diamondPos, NBT_MODULE_QUARRY_DIAMOND_POS);
                this.setPos(machine, machinePos.offset(diamondDir.rotateYCCW()), NBT_MODULE_QUARRY_REDSTONE_POS);
                this.setInt(machine, diamondDir.rotateY().getIndex(), NBT_MODULE_QUARRY_START_POS_DIRECTION_ID);

                this.setOutputStoragePos(machine, machinePos.offset(diamondDir.getOpposite()));
                this.setInputStoragePos(machine, machinePos.offset(Direction.UP));
                this.setEnergySourcePos(machine, machinePos.offset(Direction.UP));

                this.setStartPos(machine, machinePos.offset(diamondDir.rotateY()));
                this.setWorkingPos(machine, machinePos.offset(diamondDir.rotateY()));

                return;
            }
        }
    }

    protected boolean checkStructure(IVMTileMachine machine) {
        return this.hasKey(machine, NBT_MODULE_QUARRY_DIAMOND_POS) && this.hasKey(machine, NBT_MODULE_QUARRY_REDSTONE_POS);
    }

    protected void work(IVMTileMachine machine) {
        // TODO: Implement Quarry operation (in single tick)
        // 1. Count redstone blocks
        // 2. Foreach redstone block perform singleOperation(...)
        //  2.1. Check if can dig - if not return
        //  2.2. Is air block - if so then go to next 'mineable' block
        //  2.3. Else if Is Bedrock - go to next column (forward or one left)
        //  2.4. Else (mine block)
        //      2.4.1. Get drops from mined block
        //      2.4.2. If block is an inventory - get content
        //      2.4.3. Try to input into the input chest or spawn as ItemEntity
        //      2.4.4. Set workingPos to AIR
        //      2.4.5. Go to next 'mineable' block
    }
}
