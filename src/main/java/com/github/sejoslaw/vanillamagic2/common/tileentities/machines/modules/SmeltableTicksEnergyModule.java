package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import com.github.sejoslaw.vanillamagic2.common.utils.ItemStackUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SmeltableTicksEnergyModule extends AbstractEnergyModule {
    public boolean canExecuteNoEnergy(IVMTileMachine machine) {
        final IInventory inv = WorldUtils.getInventory(machine.getWorld(), this.getInputStoragePos(machine));

        if (inv == null) {
            return false;
        }

        final double oneOperationCost = VMForgeConfig.TILE_MACHINE_ONE_OPERATION_COST.get();
        final List<Integer> slotsToRemove = new ArrayList<>();

        double ticks = 0;

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            double cost = ItemStackUtils.getBurnTicks(stack);

            if (cost == 0) {
                continue;
            }

            int stackSize = stack.getCount();

            while (ticks < oneOperationCost && stackSize > 0) {
                stackSize--;
                ticks += cost;
            }

            if (stackSize == 0) {
                slotsToRemove.add(i);
            }

            if (ticks >= oneOperationCost) {
                stack.setCount(stackSize);
                slotsToRemove.forEach(inv::removeStackFromSlot);
                this.setEnergyLevel(machine, (int)(ticks / oneOperationCost));
                return true;
            }
        }

        return false;
    }
}
