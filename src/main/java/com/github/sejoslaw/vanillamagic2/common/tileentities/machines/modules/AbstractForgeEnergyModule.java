package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class AbstractForgeEnergyModule extends AbstractEnergyModule {
    protected boolean checkForgeEnergyStorage(IVMTileMachine machine, IEnergyStorage energyStorage) {
        if (energyStorage == null || !energyStorage.canExtract()) {
            return false;
        }

        final double oneOperationCost = VMForgeConfig.TILE_MACHINE_ONE_OPERATION_COST.get();

        if (energyStorage.extractEnergy((int)oneOperationCost, true) != (int)oneOperationCost) {
            return false;
        }

        if (energyStorage.extractEnergy((int)oneOperationCost, false) == (int)oneOperationCost) {
            this.setEnergyLevel(machine, 1);
            return true;
        }

        return false;
    }
}
