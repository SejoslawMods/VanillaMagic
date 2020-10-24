package com.github.sejoslaw.vanillamagic2.common.spells.summon.logics;

import com.github.sejoslaw.vanillamagic2.common.spells.summon.SpellSummonEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class WitherSkeletonSummonSpell extends SpellSummonEntity<WitherSkeletonEntity> {
    public WitherSkeletonSummonSpell() {
        super(EntityType.WITHER_SKELETON);
    }

    protected void modifyEntity(Entity entity) {
        this.setItemStackToMainHand(entity, new ItemStack(Items.DIAMOND_SWORD));
    }
}
