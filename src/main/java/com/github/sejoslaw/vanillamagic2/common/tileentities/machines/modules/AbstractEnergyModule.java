package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class AbstractEnergyModule extends AbstractMachineModule {
    public boolean canExecute(IVMTileMachine machine) {
        return this.getHasEnergy(machine) || this.canExecuteNoEnergy(machine);
    }

    public void execute(IVMTileMachine machine) {
    }

    protected abstract boolean canExecuteNoEnergy(IVMTileMachine machine);
}
