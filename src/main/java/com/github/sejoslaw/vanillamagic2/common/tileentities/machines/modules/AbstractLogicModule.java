package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class AbstractLogicModule extends AbstractMachineModule {
    public boolean canExecute(IVMTileMachine machine) {
        return this.getHasEnergy(machine) && this.checkStructure(machine);
    }

    public void execute(IVMTileMachine machine) {
        this.setHasEnergy(machine, false);
        this.work(machine);
    }

    /**
     * @return True if the machine was build correctly; otherwise false.
     */
    protected abstract boolean checkStructure(IVMTileMachine machine);

    /**
     * Performs machine single operation cycle.
     */
    protected abstract void work(IVMTileMachine machine);
}
