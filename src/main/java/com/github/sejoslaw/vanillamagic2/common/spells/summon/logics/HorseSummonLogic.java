package com.github.sejoslaw.vanillamagic2.common.spells.summon.logics;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.horse.DonkeyEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.passive.horse.MuleEntity;
import net.minecraft.world.World;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class HorseSummonLogic extends SummonEntityLogic {
    public HorseSummonLogic() {
        super(EntityType.HORSE);
    }

    public Entity getEntity(World world) {
        Entity entity;
        int rand = this.getPercent();

        if (rand < 30) {
            entity = new DonkeyEntity(EntityType.DONKEY, world);
        } else if (rand < 60) {
            entity = new MuleEntity(EntityType.MULE, world);
        } else {
            entity = new HorseEntity(EntityType.HORSE, world);
        }

        return entity;
    }
}
