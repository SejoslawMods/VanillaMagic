package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.breeders;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.AbstractSimpleMachineLogicModule;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class AbstractBreederLogicModule extends AbstractSimpleMachineLogicModule {
    public void setup(IVMTileMachine machine) {
        super.setup(machine);
        this.setupInternals("VM Breeder", () -> VMForgeConfig.BREEDER_SIZE.get());
    }
}
