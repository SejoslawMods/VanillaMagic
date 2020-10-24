package com.github.sejoslaw.vanillamagic2.common.spells.summon.logics;

import com.github.sejoslaw.vanillamagic2.common.spells.summon.SpellSummonEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class PiglinSummonSpell extends SpellSummonEntity<PiglinEntity> {
    public PiglinSummonSpell() {
        super(EntityType.PIGLIN);
    }

    protected void modifyEntity(Entity entity) {
        ItemStack stack;

        if (this.getPercent() > 50) {
            stack = new ItemStack(Items.GOLDEN_SWORD);
        } else {
            stack = new ItemStack(Items.CROSSBOW);
        }

        this.setItemStackToMainHand(entity, stack);
    }
}
