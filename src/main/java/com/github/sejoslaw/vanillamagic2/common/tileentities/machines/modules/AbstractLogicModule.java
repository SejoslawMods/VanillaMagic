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
        this.work(machine);

        int energyLevel = this.getEnergyLevel(machine);
        this.setEnergyLevel(machine, energyLevel - 1);
    }

    /**
     * @return True if the machine was build correctly; otherwise false.
     */
    protected abstract boolean checkStructure(IVMTileMachine machine);

    /**
     * Performs machine single cycle.
     */
    protected abstract void work(IVMTileMachine machine);
}
