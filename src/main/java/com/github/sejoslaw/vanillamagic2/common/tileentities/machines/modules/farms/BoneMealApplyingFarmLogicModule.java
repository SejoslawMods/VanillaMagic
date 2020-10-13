package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.farms;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.Items;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class BoneMealApplyingFarmLogicModule extends AbstractFarmLogicModule {
    protected void work(IVMTileMachine machine) {
        this.executeLogic(machine,
            (world, stack, pos) -> !world.isAirBlock(pos) && stack.getItem() == Items.BONE_MEAL,
            (world, stack, pos) -> BoneMealItem.applyBonemeal(stack, WorldUtils.asWorld(world), pos, this.getFakePlayer(world)));
    }
}
