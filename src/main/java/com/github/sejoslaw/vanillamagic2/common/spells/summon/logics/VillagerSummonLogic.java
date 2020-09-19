package com.github.sejoslaw.vanillamagic2.common.spells.summon.logics;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Collection;
import java.util.Random;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VillagerSummonLogic extends SummonEntityLogic {
    public VillagerSummonLogic() {
        super(EntityType.VILLAGER);
    }

    public Entity getEntity(World world) {
        if (world.isRemote) {
            return null;
        }

        Collection<VillagerProfession> professions = ForgeRegistries.PROFESSIONS.getValues();
        VillagerProfession profession = (VillagerProfession) professions.toArray()[new Random().nextInt(professions.size())];
        VillagerEntity entity = EntityType.VILLAGER.create(world);
        entity.setVillagerData(entity.getVillagerData().withProfession(profession));
        return entity;
    }
}
