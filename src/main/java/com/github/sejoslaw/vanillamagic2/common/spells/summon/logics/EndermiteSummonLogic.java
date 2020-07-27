package com.github.sejoslaw.vanillamagic2.common.spells.summon.logics;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.EndermiteEntity;
import net.minecraft.world.World;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EndermiteSummonLogic extends SummonEntityLogic {
    public EndermiteSummonLogic() {
        super(EntityType.ENDERMITE);
    }

    public Entity getEntity(World world) {
        EndermiteEntity entity = new EndermiteEntity(EntityType.ENDERMITE, world);
        entity.setSpawnedByPlayer(true);
        return entity;
    }
}
