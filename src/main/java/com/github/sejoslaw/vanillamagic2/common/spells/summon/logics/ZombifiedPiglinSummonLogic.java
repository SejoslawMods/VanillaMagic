package com.github.sejoslaw.vanillamagic2.common.spells.summon.logics;

import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.ZombifiedPiglinEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.IWorld;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ZombifiedPiglinSummonLogic extends SummonEntityLogic {
    public ZombifiedPiglinSummonLogic() {
        super(EntityType.ZOMBIFIED_PIGLIN);
    }

    public Entity getEntity(IWorld world) {
        ZombifiedPiglinEntity entity = EntityType.ZOMBIFIED_PIGLIN.create(WorldUtils.asWorld(world));
        entity.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
        return entity;
    }
}
