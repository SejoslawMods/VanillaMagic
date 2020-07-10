package com.github.sejoslaw.vanillamagic.common.magic.spell.spells.summon.hostile;

import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.ElderGuardianEntity;
import net.minecraft.entity.monster.GuardianEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellSummonGuardian extends SpellSummonHostile {
    public SpellSummonGuardian(int spellID, String spellName, String spellUniqueName, IWand wand, ItemStack itemOffHand) {
        super(spellID, spellName, spellUniqueName, wand, itemOffHand);
    }

    public Entity getEntity(World world) {
        Entity entity;

        if (this.getPercent() < 30) {
            entity = new ElderGuardianEntity(EntityType.ELDER_GUARDIAN, world);
        } else {
            entity = new GuardianEntity(EntityType.GUARDIAN, world);
        }

        return entity;
    }
}