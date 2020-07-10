package com.github.sejoslaw.vanillamagic.common.magic.spell.spells.summon.passive;

import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.horse.DonkeyEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.passive.horse.MuleEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellSummonHorse extends SpellSummonPassive {
    public SpellSummonHorse(int spellID, String spellName, String spellUniqueName, IWand wand, ItemStack itemOffHand) {
        super(spellID, spellName, spellUniqueName, wand, itemOffHand);
    }

    public Entity getEntity(World world) {
        Entity entity;
        int rand = this.getPercent();

        if (rand < 30) {
            entity = new DonkeyEntity(EntityType.DONKEY, world);
        } else if (rand > 30 && rand < 60) {
            entity = new MuleEntity(EntityType.MULE, world);
        } else {
            entity = new HorseEntity(EntityType.HORSE, world);
        }

        return entity;
    }
}