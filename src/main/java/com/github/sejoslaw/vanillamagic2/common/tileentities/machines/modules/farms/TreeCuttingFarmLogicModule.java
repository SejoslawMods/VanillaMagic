package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.farms;

import com.github.sejoslaw.vanillamagic2.common.quests.eventcallers.EventCallerFullTreeCut;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class TreeCuttingFarmLogicModule extends AbstractFarmLogicModule {
    protected void work(IVMTileMachine machine) {
        this.executeLogic(machine,
            (world, stack, pos) -> EventCallerFullTreeCut.isLog(world, pos) && EventCallerFullTreeCut.isAxe(stack),
            (world, stack, pos) -> new EventCallerFullTreeCut.TreeChopTask(this.getFakePlayer(world), world, pos, stack).execute());
    }
}
