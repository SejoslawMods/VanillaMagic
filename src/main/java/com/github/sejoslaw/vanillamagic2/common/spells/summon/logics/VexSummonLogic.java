package com.github.sejoslaw.vanillamagic2.common.spells.summon.logics;

import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.IWorld;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VexSummonLogic extends SummonEntityLogic {
    public VexSummonLogic() {
        super(EntityType.VEX);
    }

    public Entity getEntity(IWorld world) {
        VexEntity vex = EntityType.VEX.create(WorldUtils.asWorld(world));
        vex.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.IRON_SWORD));
        return vex;
    }
}
