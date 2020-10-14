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
    public void setup(IVMTileMachine machine) {
        super.setup(machine);
        this.setupInternals("VM Killer", () -> VMForgeConfig.KILLER_SIZE.get());
    }

    protected boolean isSword(ItemStack stack) {
        return stack.getItem() instanceof SwordItem;
    }

    protected float getAttackDamage(ItemStack stack) {
        return ((SwordItem)stack.getItem()).getAttackDamage();
    }
}
