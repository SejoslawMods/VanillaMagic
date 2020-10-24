package com.github.sejoslaw.vanillamagic2.common.spells.summon.logics;

import com.github.sejoslaw.vanillamagic2.common.spells.summon.SpellSummonEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.SlimeEntity;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SlimeSummonSpell extends SpellSummonEntity<SlimeEntity> {
    public SlimeSummonSpell() {
        super(EntityType.SLIME);
    }

    protected void modifyEntity(Entity entity) {
        this.setSlimeData(entity);
    }
}
