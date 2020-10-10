package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.farms;

import com.github.sejoslaw.vanillamagic2.common.quests.eventcallers.EventCallerFullTreeCut;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class TreeCuttingFarmLogicModule extends AbstractFarmLogicModule {
    protected void work(IVMTileMachine machine) {
        this.executeLogic(machine,
            (world, stack, pos) -> EventCallerFullTreeCut.isLog(world, pos),
            (world, stack, pos) -> new EventCallerFullTreeCut.TreeChopTask(this.getFarmer(world), world, pos, new ItemStack(Items.NETHERITE_AXE)));
    }
}
