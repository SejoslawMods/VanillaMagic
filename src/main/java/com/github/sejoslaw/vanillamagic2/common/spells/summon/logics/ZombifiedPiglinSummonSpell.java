package com.github.sejoslaw.vanillamagic2.common.spells.summon.logics;

import com.github.sejoslaw.vanillamagic2.common.spells.summon.SpellSummonEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ZombifiedPiglinSummonSpell extends SpellSummonEntity<ZombifiedPiglinEntity> {
    public ZombifiedPiglinSummonSpell() {
        super(EntityType.ZOMBIFIED_PIGLIN);
    }

    protected void modifyEntity(Entity entity) {
        this.setItemStackToMainHand(entity, new ItemStack(Items.GOLDEN_SWORD));
    }
}
