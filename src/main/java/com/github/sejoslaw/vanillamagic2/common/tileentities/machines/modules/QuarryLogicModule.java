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
        BlockPos diamondPos = null;
        BlockPos redstonePos = null;
        BlockPos outputInvPos = null;
        BlockPos inputInvPos = null;

        for (Direction face : Direction.values()) {
            BlockPos facePos = machinePos.offset(face);

            if (machine.getWorld().getBlockState(facePos).getBlock() == Blocks.DIAMOND_BLOCK) {
                diamondPos = facePos;
                redstonePos = machinePos.offset(face.rotateYCCW());
                outputInvPos = machinePos.offset(face.getOpposite());
                inputInvPos = machinePos.offset(Direction.UP);
                break;
            }
        }

        if (diamondPos == null) {
            return;
        }

        this.setPos(machine, diamondPos, NBT_MODULE_QUARRY_DIAMOND_POS);
        this.setPos(machine, redstonePos, NBT_MODULE_QUARRY_REDSTONE_POS);

        this.setOutputStoragePos(machine, outputInvPos);
        this.setInputStoragePos(machine, inputInvPos);
        this.setEnergySourcePos(machine, inputInvPos);
    }

    protected boolean checkStructure(IVMTileMachine machine) {
        return this.hasKey(machine, NBT_MODULE_QUARRY_DIAMOND_POS) && this.hasKey(machine, NBT_MODULE_QUARRY_REDSTONE_POS);
    }

    protected void work(IVMTileMachine machine) {
    }
}
