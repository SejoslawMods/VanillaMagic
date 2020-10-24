package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.farms;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class HoeFindingFarmLogicModule extends AbstractFarmLogicModule {
    protected void work(IVMTileMachine machine) {
        IInventory inv = this.getInventory(machine);

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);

            if (this.isHoe(stack)) {
                this.setSlotId(machine, i);
            }
        }

        this.setSlotId(machine, 0);
    }
}
