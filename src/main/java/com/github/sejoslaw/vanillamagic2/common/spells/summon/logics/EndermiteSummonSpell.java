package com.github.sejoslaw.vanillamagic2.common.spells.summon.logics;

import com.github.sejoslaw.vanillamagic2.common.spells.summon.SpellSummonEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.EndermiteEntity;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EndermiteSummonSpell extends SpellSummonEntity<EndermiteEntity> {
    public EndermiteSummonSpell() {
        super(EntityType.ENDERMITE);
    }

    protected void modifyEntity(Entity entity) {
        ((EndermiteEntity) entity).setSpawnedByPlayer(true);
    }
}
