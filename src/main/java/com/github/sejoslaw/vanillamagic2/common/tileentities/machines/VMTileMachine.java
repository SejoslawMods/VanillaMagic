package com.github.sejoslaw.vanillamagic2.common.tileentities.machines;

import com.github.sejoslaw.vanillamagic2.common.registries.MachineModuleRegistry;
import com.github.sejoslaw.vanillamagic2.common.tileentities.VMTileEntity;
import com.github.sejoslaw.vanillamagic2.common.utils.NbtUtils;
import com.github.sejoslaw.vanillamagic2.core.VMTiles;
import net.minecraft.nbt.CompoundNBT;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VMTileMachine extends VMTileEntity implements IVMTileMachine {
    private String moduleKey;

    public VMTileMachine() {
        super(VMTiles.MACHINE);
    }

    public void setModuleKey(String key) {
        this.moduleKey = key;
    }

    public CompoundNBT write(CompoundNBT nbt) {
        super.write(nbt);
        nbt.putString(NbtUtils.NBT_MACHINE_MODULE_KEY, this.moduleKey);
        return nbt;
    }

    public void read(CompoundNBT nbt) {
        super.read(nbt);
        this.moduleKey = nbt.getString(NbtUtils.NBT_MACHINE_MODULE_KEY);
    }

    public void tick() {
        if (MachineModuleRegistry.DEFAULT_MODULES.stream().allMatch(module -> module.canExecute(this))) {
            MachineModuleRegistry.DEFAULT_MODULES.forEach(module -> module.execute(this));
        } else {
            return;
        }

        if (MachineModuleRegistry.MODULES.get(this.moduleKey).stream().allMatch(machine -> machine.canExecute(this))) {
            MachineModuleRegistry.MODULES.get(this.moduleKey).forEach(machine -> machine.execute(this));
        }
    }
}
