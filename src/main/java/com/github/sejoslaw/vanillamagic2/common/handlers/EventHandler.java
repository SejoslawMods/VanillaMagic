package com.github.sejoslaw.vanillamagic2.common.handlers;

import com.github.sejoslaw.vanillamagic2.common.functions.Consumer2;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class EventHandler {
    public void onLivingDeath(LivingDeathEvent event, Consumer2<LivingEntity, DamageSource> consumer) {
        LivingEntity entity = event.getEntityLiving();
        DamageSource source = event.getSource();

        consumer.accept(entity, source);
    }
}
