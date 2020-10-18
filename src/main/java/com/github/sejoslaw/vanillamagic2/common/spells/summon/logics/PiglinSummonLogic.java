package com.github.sejoslaw.vanillamagic2.common.spells.summon.logics;

import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.IWorld;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class PiglinSummonLogic extends SummonEntityLogic {
    public PiglinSummonLogic() {
        super(EntityType.PIGLIN);
    }

    public Entity getEntity(IWorld world) {
        PiglinEntity entity = new PiglinEntity(EntityType.PIGLIN, WorldUtils.asWorld(world));

        if (world.getRandom().nextInt(100) > 50) {
            entity.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
        } else {
            entity.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.CROSSBOW));
        }

        return entity;
    }
}
