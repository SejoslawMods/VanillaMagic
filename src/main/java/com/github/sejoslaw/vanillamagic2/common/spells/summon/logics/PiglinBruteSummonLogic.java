package com.github.sejoslaw.vanillamagic2.common.spells.summon.logics;

import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.piglin.PiglinBruteEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.IWorld;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class PiglinBruteSummonLogic extends SummonEntityLogic {
    public PiglinBruteSummonLogic() {
        super(EntityType.field_242287_aj);
    }

    public Entity getEntity(IWorld world) {
        PiglinBruteEntity entity = EntityType.field_242287_aj.create(WorldUtils.asWorld(world));
        entity.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_AXE));
        return entity;
    }
}
