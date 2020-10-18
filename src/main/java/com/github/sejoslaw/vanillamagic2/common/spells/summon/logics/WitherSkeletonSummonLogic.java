package com.github.sejoslaw.vanillamagic2.common.spells.summon.logics;

import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.IWorld;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class WitherSkeletonSummonLogic extends SummonEntityLogic {
    public WitherSkeletonSummonLogic() {
        super(EntityType.WITHER_SKELETON);
    }

    public Entity getEntity(IWorld world) {
        WitherSkeletonEntity entity = EntityType.WITHER_SKELETON.create(WorldUtils.asWorld(world));
        entity.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.DIAMOND_SWORD));
        return entity;
    }
}
