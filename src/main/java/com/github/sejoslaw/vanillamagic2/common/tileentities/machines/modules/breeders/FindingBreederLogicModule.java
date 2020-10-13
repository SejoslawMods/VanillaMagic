package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.breeders;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class FindingBreederLogicModule extends AbstractBreederLogicModule {
    protected void work(IVMTileMachine machine) {
        this.useSlot(machine, (inv, slotId, stack) -> {
            slotId++;

            if (slotId >= inv.getSizeInventory()) {
                slotId = 0;
            }

            this.setSlotId(machine, slotId);
        });
    }
}
