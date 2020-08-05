package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import net.minecraft.util.math.BlockPos;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class WorkingPositionModule extends AbstractMachineModule {
    public boolean canExecute(IVMTileMachine machine) {
        if (this.getWorkingPos(machine).equals(BlockPos.ZERO)) {
            this.setWorkingPos(machine, machine.getPos());
        }

        if (this.getStartPos(machine).equals(BlockPos.ZERO)) {
            this.setStartPos(machine, machine.getPos());
        }

        return true;
    }

    public void execute(IVMTileMachine machine) {
    }
}
