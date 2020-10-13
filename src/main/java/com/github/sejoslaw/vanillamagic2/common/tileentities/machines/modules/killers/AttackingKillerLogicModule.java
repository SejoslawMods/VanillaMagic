package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.killers;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IWorld;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class AttackingKillerLogicModule extends AbstractKillerLogicModule {
    protected void work(IVMTileMachine machine) {
        int slotId = this.getSwordSlotId(machine);
        IInventory inv = this.getInventory(machine);
        ItemStack stack = inv.getStackInSlot(slotId);

        if (!this.isSword(stack)) {
            return;
        }

        IWorld world = machine.getWorld();
        WorldUtils.getEntities(world, LivingEntity.class, machine.getPos(), this.getSize(machine), entity -> true)
                .forEach(livingEntity -> stack.hitEntity(livingEntity, this.getFakePlayer(world)));
    }
}
