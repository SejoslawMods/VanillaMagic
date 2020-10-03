package com.github.sejoslaw.vanillamagic2.common.spells.summon.logics;

import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.CaveSpiderEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.world.IWorld;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpiderSummonLogic extends SummonEntityLogic {
    public SpiderSummonLogic() {
        super(EntityType.SPIDER);
    }

    public Entity getEntity(IWorld world) {
        Entity entity;

        if (this.getPercent() < 70) {
            entity = new SpiderEntity(EntityType.SPIDER, WorldUtils.asWorld(world));
        } else if (this.getPercent() < 90) {
            entity = new CaveSpiderEntity(EntityType.CAVE_SPIDER, WorldUtils.asWorld(world));
        } else {
            entity = this.getSpiderJockey(world);
        }

        return entity;
    }

    private Entity getSpiderJockey(IWorld world) {
        Entity spiderEntity = EntityType.SPIDER.create(WorldUtils.asWorld(world));
        Entity skeletonEntity = EntityType.SKELETON.create(WorldUtils.asWorld(world));

        skeletonEntity.startRiding(spiderEntity);
        world.addEntity(skeletonEntity);

        return spiderEntity;
    }
}
