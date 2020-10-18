package com.github.sejoslaw.vanillamagic2.common.spells.summon.logics;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.IWorld;

import java.util.Random;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class SummonEntityLogic {
    public final EntityType<? extends Entity> entityType;

    public SummonEntityLogic(EntityType<? extends Entity> entityType) {
        this.entityType = entityType;
    }

    public Entity getEntity(IWorld world) {
        return null;
    }

    public Entity getHorse(IWorld world) {
        return null;
    }

    public int getPercent() {
        return new Random().nextInt(100);
    }
}
