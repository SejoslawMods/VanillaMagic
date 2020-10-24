package com.github.sejoslaw.vanillamagic2.common.spells.summon.logics;

import com.github.sejoslaw.vanillamagic2.common.spells.summon.SpellSummonEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;
import java.util.Random;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VillagerSummonSpell extends SpellSummonEntity<VillagerEntity> {
    public VillagerSummonSpell() {
        super(EntityType.VILLAGER);
    }

    protected void modifyEntity(Entity entity) {
        Collection<VillagerProfession> professions = ForgeRegistries.PROFESSIONS.getValues();
        VillagerProfession profession = (VillagerProfession) professions.toArray()[new Random().nextInt(professions.size())];
        ((VillagerEntity)entity).setVillagerData(((VillagerEntity)entity).getVillagerData().withLevel(2).withProfession(profession));
    }
}
