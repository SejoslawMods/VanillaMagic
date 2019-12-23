package com.github.sejoslaw.vanillamagic.common.magic.spell.spells.summon.passive;

import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;
import java.util.Random;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellSummonVillager extends SpellSummonPassive {
    public SpellSummonVillager(int spellID, String spellName, String spellUniqueName, IWand wand, ItemStack itemOffHand) {
        super(spellID, spellName, spellUniqueName, wand, itemOffHand);
    }

    public Entity getEntity(World world) {
        VillagerEntity entity = new VillagerEntity(EntityType.VILLAGER, world);
        entity.setVillagerData(entity.getVillagerData().withProfession(this.getRandomProfession()));
        return entity;
    }

    private VillagerProfession getRandomProfession() {
        Collection<VillagerProfession> professions = ForgeRegistries.PROFESSIONS.getValues();
        int professionsCount = professions.size();
        int random = new Random().nextInt(professionsCount);
        return (VillagerProfession) professions.stream().toArray()[random];
    }
}