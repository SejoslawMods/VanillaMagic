package com.github.sejoslaw.vanillamagic2.common.spells.summon.logics;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.ZombiePigmanEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class PigmanSummonLogic extends SummonEntityLogic {
    public PigmanSummonLogic() {
        super(EntityType.ZOMBIE_PIGMAN);
    }

    public Entity getEntity(World world) {
        ZombiePigmanEntity entity = new ZombiePigmanEntity(EntityType.ZOMBIE_PIGMAN, world);
        entity.setItemStackToSlot(EquipmentSlotType.MAINHAND, new ItemStack(Items.GOLDEN_SWORD));
        return entity;
    }
}
