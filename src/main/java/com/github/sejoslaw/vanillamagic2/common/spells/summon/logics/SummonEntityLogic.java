package com.github.sejoslaw.vanillamagic2.common.spells.summon.logics;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

import java.util.Random;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SummonEntityLogic {
    public final EntityType<? extends Entity> entityType;

    public SummonEntityLogic(EntityType<? extends Entity> entityType) {
        this.entityType = entityType;
    }

    public Entity getEntity(World world) {
        return null;
    }

    public Entity getHorse(World world) {
        return null;
    }

    public int getPercent() {
        return new Random().nextInt(100);
    }
}
