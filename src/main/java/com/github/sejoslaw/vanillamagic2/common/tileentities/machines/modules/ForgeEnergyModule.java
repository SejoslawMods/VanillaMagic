package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ForgeEnergyModule extends AbstractEnergyModule {
    protected boolean canExecuteNoEnergy(IVMTileMachine machine) {
        IEnergyStorage energyStorage = (IEnergyStorage) machine.getWorld().getTileEntity(this.getEnergySourcePos(machine));

        if (energyStorage == null || !energyStorage.canExtract()) {
            return false;
        }

        final double oneOperationCost = VMForgeConfig.TILE_MACHINE_ONE_OPERATION_COST.get();

        if (energyStorage.extractEnergy((int)oneOperationCost, true) != (int)oneOperationCost) {
            return false;
        }

        if (energyStorage.extractEnergy((int)oneOperationCost, false) == (int)oneOperationCost) {
            this.setHasEnergy(machine, true);
            return true;
        }

        return false;
    }
}
