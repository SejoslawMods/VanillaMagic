package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class AbstractLogicModule extends AbstractMachineModule {
    public boolean canExecute(IVMTileMachine machine) {
        return this.getHasEnergy(machine);
    }

    public void execute(IVMTileMachine machine) {
        this.setHasEnergy(machine, false);
        this.work(machine);
    }

    protected abstract void work(IVMTileMachine machine);
}
