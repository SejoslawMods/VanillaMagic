package com.github.sejoslaw.vanillamagic.common.magic.spell.spells.summon.hostile;

import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellSummonVindicator extends SpellSummonHostile {
    public SpellSummonVindicator(int spellID, String spellName, String spellUniqueName, IWand wand, ItemStack itemOffHand) {
        super(spellID, spellName, spellUniqueName, wand, itemOffHand);
    }

    public Entity getEntity(World world) {
        return new VindicatorEntity(EntityType.VINDICATOR, world);
    }
}