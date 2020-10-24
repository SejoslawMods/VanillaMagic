package com.github.sejoslaw.vanillamagic2.common.spells.summon.logics;

import com.github.sejoslaw.vanillamagic2.common.spells.summon.SpellSummonEntity;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.ElderGuardianEntity;
import net.minecraft.entity.monster.GuardianEntity;
import net.minecraft.world.IWorld;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class GuardianSummonSpell extends SpellSummonEntity<GuardianEntity> {
    public GuardianSummonSpell() {
        super(EntityType.GUARDIAN);
    }

    protected void fillEntitiesToSpawn(IWorld world, List<Entity> entities) {
        if (this.getPercent() < 30) {
            entities.add(new ElderGuardianEntity(EntityType.ELDER_GUARDIAN, WorldUtils.asWorld(world)));
        } else {
            entities.add(new GuardianEntity(EntityType.GUARDIAN, WorldUtils.asWorld(world)));
        }
    }
}
