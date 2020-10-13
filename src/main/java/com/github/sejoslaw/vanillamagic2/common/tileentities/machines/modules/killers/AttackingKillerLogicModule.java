package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.killers;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import net.minecraft.entity.LivingEntity;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class AttackingKillerLogicModule extends AbstractKillerLogicModule {
    protected void work(IVMTileMachine machine) {
        this.useSlot(machine, (inv, slotId, stack) -> {
            if (!this.isSword(stack)) {
                return;
            }

            this.getEntities(machine, LivingEntity.class, entity -> true)
                    .forEach(livingEntity -> stack.hitEntity(livingEntity, this.getFakePlayer(machine.getWorld())));
        });
    }
}
