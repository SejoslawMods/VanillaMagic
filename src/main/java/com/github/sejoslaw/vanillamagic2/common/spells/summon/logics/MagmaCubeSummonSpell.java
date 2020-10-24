package com.github.sejoslaw.vanillamagic2.common.spells.summon.logics;

import com.github.sejoslaw.vanillamagic2.common.spells.summon.SpellSummonEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.MagmaCubeEntity;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class MagmaCubeSummonSpell extends SpellSummonEntity<MagmaCubeEntity> {
    public MagmaCubeSummonSpell() {
        super(EntityType.MAGMA_CUBE);
    }

    protected void modifyEntity(Entity entity) {
        this.setSlimeData(entity);
    }
}
