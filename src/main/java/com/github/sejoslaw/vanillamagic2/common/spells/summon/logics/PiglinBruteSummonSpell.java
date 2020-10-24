package com.github.sejoslaw.vanillamagic2.common.spells.summon.logics;

import com.github.sejoslaw.vanillamagic2.common.spells.summon.SpellSummonEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.piglin.PiglinBruteEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class PiglinBruteSummonSpell extends SpellSummonEntity<PiglinBruteEntity> {
    public PiglinBruteSummonSpell() {
        super(EntityType.field_242287_aj);
    }

    protected void modifyEntity(Entity entity) {
        this.setItemStackToMainHand(entity, new ItemStack(Items.GOLDEN_AXE));
    }
}
