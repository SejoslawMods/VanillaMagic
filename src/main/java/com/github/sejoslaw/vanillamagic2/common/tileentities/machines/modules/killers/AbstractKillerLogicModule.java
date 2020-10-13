package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.killers;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.AbstractSimpleMachineLogicModule;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class AbstractKillerLogicModule extends AbstractSimpleMachineLogicModule {
    private static final String NBT_MODULE_SWORD_SLOT_ID = "NBT_MODULE_SWORD_SLOT_ID";

    public void setup(IVMTileMachine machine) {
        super.setup(machine);
        this.setupInternals("VM Killer", () -> VMForgeConfig.KILLER_SIZE.get());
    }

    protected void setSwordSlotId(IVMTileMachine machine, int slotId) {
        this.setInt(machine, slotId, NBT_MODULE_SWORD_SLOT_ID);
    }

    protected int getSwordSlotId(IVMTileMachine machine) {
        return this.getInt(machine, NBT_MODULE_SWORD_SLOT_ID);
    }

    protected boolean isSword(ItemStack stack) {
        return stack.getItem() instanceof SwordItem || stack.getItem().getRegistryName().toString().toLowerCase().contains("_sword");
    }
}
