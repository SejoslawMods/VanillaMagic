package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.farms;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import net.minecraft.block.BlockState;
import net.minecraft.item.HoeItem;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SoilShapingFarmLogicModule extends AbstractFarmLogicModule {
    protected void work(IVMTileMachine machine) {
        this.useSlot(machine, (inv, slotId, stack) -> {
            if (!this.isHoe(stack)) {
                return;
            }

            IWorld world = machine.getWorld();
            BlockPos workingPos = this.getWorkingPos(machine);

            if (workingPos.equals(machine.getPos())) {
                return;
            }

            BlockPos groundPos = workingPos.offset(Direction.DOWN);
            BlockState groundState = world.getBlockState(groundPos);
            BlockState soilState = HoeItem.getHoeTillingState(groundState);

            if (soilState == null) {
                return;
            }

            stack.damageItem(1, this.getFakePlayer(world), fakePlayer -> { });
            world.setBlockState(groundPos, soilState, 1 | 2);
        });
    }
}
