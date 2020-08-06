package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ForgeCapabilityEnergyModule extends AbstractForgeEnergyModule {
    protected boolean canExecuteNoEnergy(IVMTileMachine machine) {
        TileEntity tile = machine.getWorld().getTileEntity(this.getEnergySourcePos(machine));
        IEnergyStorage energyStorage = tile.getCapability(CapabilityEnergy.ENERGY).orElse(null);
        return this.checkForgeEnergyStorage(machine, energyStorage);
    }
}
