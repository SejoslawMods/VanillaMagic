package com.github.sejoslaw.vanillamagic2.common.spells.summon.logics;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.CaveSpiderEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.world.World;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpiderSummonLogic extends SummonEntityLogic {
    public SpiderSummonLogic() {
        super(EntityType.SPIDER);
    }

    public Entity getEntity(World world) {
        Entity entity;

        if (this.getPercent() < 70) {
            entity = new SpiderEntity(EntityType.SPIDER, world);
        } else if (this.getPercent() < 90) {
            entity = new CaveSpiderEntity(EntityType.CAVE_SPIDER, world);
        } else {
            entity = this.getSpiderJockey(world);
        }

        return entity;
    }

    private Entity getSpiderJockey(World world) {
        Entity spiderEntity = EntityType.SPIDER.create(world);
        Entity skeletonEntity = EntityType.SKELETON.create(world);

        skeletonEntity.startRiding(spiderEntity);
        world.addEntity(skeletonEntity);

        return spiderEntity;
    }
}
