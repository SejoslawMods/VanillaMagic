package com.github.sejoslaw.vanillamagic2.common.spells.summon.logics;

import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.StrayEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.IWorld;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class StraySummonLogic extends SummonEntityLogic {
    public StraySummonLogic() {
        super(EntityType.STRAY);
    }

    public Entity getEntity(IWorld world) {
        StrayEntity stray = EntityType.STRAY.create(WorldUtils.asWorld(world));
        stray.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.BOW));
        return stray;
    }
}
