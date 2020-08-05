package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IMachineModule;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import com.github.sejoslaw.vanillamagic2.common.utils.NbtUtils;
import net.minecraft.nbt.CompoundNBT;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class WorkingPositionModule implements IMachineModule {
    public boolean canExecute(IVMTileMachine machine) {
        CompoundNBT nbt = machine.getTileData();

        if (!nbt.hasUniqueId(NbtUtils.NBT_MODULE_WORKING_POS)) {
            nbt.putLong(NbtUtils.NBT_MODULE_WORKING_POS, machine.getPos().toLong());
        }

        if (!nbt.hasUniqueId(NbtUtils.NBT_MODULE_START_POS)) {
            nbt.putLong(NbtUtils.NBT_MODULE_START_POS, machine.getPos().toLong());
        }

        return true;
    }

    public void execute(IVMTileMachine machine) {
    }
}
