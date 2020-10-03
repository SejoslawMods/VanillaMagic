package com.github.sejoslaw.vanillamagic2.common.spells.summon.logics;

import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.passive.horse.SkeletonHorseEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.IWorld;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SkeletonSummonLogic extends SummonEntityLogic {
    public SkeletonSummonLogic() {
        super(EntityType.SKELETON);
    }

    public Entity getEntity(IWorld world) {
        SkeletonEntity entity = new SkeletonEntity(EntityType.SKELETON, WorldUtils.asWorld(world));
        entity.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));
        return entity;
    }

    public Entity getHorse(IWorld world) {
        return new SkeletonHorseEntity(EntityType.SKELETON_HORSE, WorldUtils.asWorld(world));
    }
}
