package com.github.sejoslaw.vanillamagic2.common.spells.summon.logics;

import com.github.sejoslaw.vanillamagic2.common.spells.summon.SpellSummonEntity;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.CaveSpiderEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.world.IWorld;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpiderSummonSpell extends SpellSummonEntity<SpiderEntity> {
    public SpiderSummonSpell() {
        super(EntityType.SPIDER);
    }

    protected void fillEntitiesToSpawn(IWorld world, List<Entity> entities) {
        if (this.getPercent() < 70) {
            entities.add(new SpiderEntity(EntityType.SPIDER, WorldUtils.asWorld(world)));
        } else if (this.getPercent() < 90) {
            entities.add(new CaveSpiderEntity(EntityType.CAVE_SPIDER, WorldUtils.asWorld(world)));
        } else {
            Entity spiderEntity = EntityType.SPIDER.create(WorldUtils.asWorld(world));
            Entity skeletonEntity = EntityType.SKELETON.create(WorldUtils.asWorld(world));

            skeletonEntity.startRiding(spiderEntity);

            entities.add(spiderEntity);
            entities.add(skeletonEntity);
        }
    }
}
