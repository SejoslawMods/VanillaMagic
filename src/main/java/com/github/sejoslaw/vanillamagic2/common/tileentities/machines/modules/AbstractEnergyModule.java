package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class AbstractEnergyModule extends AbstractMachineModule {
    public boolean canExecute(IVMTileMachine machine) {
        if(this.getHasEnergy(machine)) {
            return true;
        }

        return this.canExecuteNoEnergy(machine);
    }

    public void execute(IVMTileMachine machine) {
    }

    protected abstract boolean canExecuteNoEnergy(IVMTileMachine machine);
}
