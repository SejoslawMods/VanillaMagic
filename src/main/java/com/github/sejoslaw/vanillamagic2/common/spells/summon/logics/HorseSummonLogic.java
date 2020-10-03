package com.github.sejoslaw.vanillamagic2.common.spells.summon.logics;

import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.horse.DonkeyEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.passive.horse.MuleEntity;
import net.minecraft.world.IWorld;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class HorseSummonLogic extends SummonEntityLogic {
    public HorseSummonLogic() {
        super(EntityType.HORSE);
    }

    public Entity getEntity(IWorld world) {
        Entity entity;
        int rand = this.getPercent();

        if (rand < 30) {
            entity = new DonkeyEntity(EntityType.DONKEY, WorldUtils.asWorld(world));
        } else if (rand < 60) {
            entity = new MuleEntity(EntityType.MULE, WorldUtils.asWorld(world));
        } else {
            entity = new HorseEntity(EntityType.HORSE, WorldUtils.asWorld(world));
        }

        return entity;
    }
}
