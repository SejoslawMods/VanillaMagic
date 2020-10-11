package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.farms;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import net.minecraft.util.math.BlockPos;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class WorkingPosMoverFarmLogicModule extends AbstractFarmLogicModule {
    protected void work(IVMTileMachine machine) {
        int size = this.getSize(machine);
        int maxX = machine.getPos().getX() + size;
        int maxZ = machine.getPos().getZ() + size;
        BlockPos startPos = this.getFarmStartPos(machine);
        BlockPos workingPos = this.getWorkingPos(machine);

        workingPos = workingPos.add(1, 0, 0);

        if (workingPos.getX() > maxX) {
            workingPos = new BlockPos(startPos.getX(), startPos.getY(), workingPos.getZ() + 1);
        }

        if (workingPos.getZ() > maxZ) {
            workingPos = startPos;
        }

        this.setWorkingPos(machine, workingPos);
    }
}
