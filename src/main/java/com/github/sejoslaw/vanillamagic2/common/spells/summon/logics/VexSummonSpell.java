package com.github.sejoslaw.vanillamagic2.common.spells.summon.logics;

import com.github.sejoslaw.vanillamagic2.common.spells.summon.SpellSummonEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VexSummonSpell extends SpellSummonEntity<VexEntity> {
    public VexSummonSpell() {
        super(EntityType.VEX);
    }

    protected void modifyEntity(Entity entity) {
        this.setItemStackToMainHand(entity, new ItemStack(Items.IRON_SWORD));
    }
}
