package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.killers;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class AttackingKillerLogicModule extends AbstractKillerLogicModule {
    protected void work(IVMTileMachine machine) {
        this.useSlot(machine, (inv, slotId, stack) -> {
            if (!this.isSword(stack)) {
                return;
            }

            this.getEntities(machine, LivingEntity.class, entity -> !(entity instanceof PlayerEntity))
                    .forEach(livingEntity -> {
                        livingEntity.attackEntityFrom(DamageSource.GENERIC, this.getAttackDamage(stack));
                        stack.attemptDamageItem(1, livingEntity.world.rand, this.getFakePlayer(machine.getWorld()));
                    });
        });
    }
}
