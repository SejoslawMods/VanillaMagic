package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import com.mojang.authlib.GameProfile;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Items;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class AbstractLogicModule extends AbstractMachineModule {
    public boolean canExecute(IVMTileMachine machine) {
        return this.getHasEnergy(machine) && this.checkStructure(machine);
    }

    public void execute(IVMTileMachine machine) {
        this.work(machine);

        int energyLevel = this.getEnergyLevel(machine);
        this.setEnergyLevel(machine, energyLevel - 1);
    }

    protected IInventory getInventory(IVMTileMachine machine) {
        return (IInventory) machine.getWorld().getTileEntity(this.getInputStoragePos(machine));
    }

    protected int getSize(IVMTileMachine machine) {
        return 1 + this.getInventory(machine).count(Items.DIAMOND_BLOCK);
    }

    protected FakePlayer getFakePlayer(IWorld world, GameProfile profile) {
        return FakePlayerFactory.get((ServerWorld) world, profile);
    }

    /**
     * @return True if the machine was build correctly; otherwise false.
     */
    protected abstract boolean checkStructure(IVMTileMachine machine);

    /**
     * Performs machine single cycle.
     */
    protected abstract void work(IVMTileMachine machine);
}
