package com.github.sejoslaw.vanillamagic.common.magic.spell.spells.summon.passive;

import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellSummonSheep extends SpellSummonPassive {
    public SpellSummonSheep(int spellID, String spellName, String spellUniqueName, IWand wand, ItemStack itemOffHand) {
        super(spellID, spellName, spellUniqueName, wand, itemOffHand);
    }

    public Entity getEntity(World world) {
        return new SheepEntity(EntityType.SHEEP, world);
    }
}