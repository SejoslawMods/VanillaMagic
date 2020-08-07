package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuarryLogicModule extends AbstractLogicModule {
    private static final String NBT_MODULE_QUARRY_DIAMOND_POS = "NBT_MODULE_QUARRY_DIAMOND_POS";
    private static final String NBT_MODULE_QUARRY_REDSTONE_POS = "NBT_MODULE_QUARRY_REDSTONE_POS";

    public void setup(IVMTileMachine machine) {
        BlockPos machinePos = machine.getPos();

        for (Direction diamondDir : Direction.values()) {
            BlockPos diamondPos = machinePos.offset(diamondDir);

            if (machine.getWorld().getBlockState(diamondPos).getBlock() == Blocks.DIAMOND_BLOCK) {
                this.setPos(machine, diamondPos, NBT_MODULE_QUARRY_DIAMOND_POS);
                this.setPos(machine, machinePos.offset(diamondDir.rotateYCCW()), NBT_MODULE_QUARRY_REDSTONE_POS);

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
        // TODO: Implement Quarry single operation
    }
}
