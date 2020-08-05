package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import net.minecraft.util.math.BlockPos;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class StorageModule extends AbstractMachineModule {
    public boolean canExecute(IVMTileMachine machine) {
        if (this.getInputStoragePos(machine).equals(BlockPos.ZERO)) {
            this.setInputStoragePos(machine, machine.getPos());
        }

        if (this.getOutputStoragePos(machine).equals(BlockPos.ZERO)) {
            this.setOutputStoragePos(machine, machine.getPos());
        }

        return true;
    }

    public void execute(IVMTileMachine machine) {
    }
}
