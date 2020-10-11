package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.farms;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ItemCollectingFarmLogicModule extends AbstractFarmLogicModule {
    protected void work(IVMTileMachine machine) {
        this.executeLogic(machine,
                (world, stack, pos) -> true,
                (world, stack, pos) -> {
                    List<ItemStack> stacks = WorldUtils.getItems(world, pos)
                            .stream()
                            .map(ItemEntity::getItem)
                            .collect(Collectors.toList());
                    WorldUtils.putStacksInInventoryAllSlots(world, this.getInventory(machine), stacks, Direction.DOWN, machine.getPos().offset(Direction.UP, 2));
                });
    }
}
