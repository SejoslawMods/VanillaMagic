package com.github.sejoslaw.vanillamagic2.common.spells.summon.logics;

import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.world.IWorld;
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

    public Entity getEntity(IWorld world) {
        if (WorldUtils.getIsRemote(world)) {
            return null;
        }

        Collection<VillagerProfession> professions = ForgeRegistries.PROFESSIONS.getValues();
        VillagerProfession profession = (VillagerProfession) professions.toArray()[new Random().nextInt(professions.size())];
        VillagerEntity entity = EntityType.VILLAGER.create(WorldUtils.asWorld(world));
        entity.setVillagerData(entity.getVillagerData().withLevel(2).withProfession(profession));
        return entity;
    }
}
