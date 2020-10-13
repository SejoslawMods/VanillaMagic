package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import com.mojang.authlib.GameProfile;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.util.FakePlayer;

import java.util.UUID;
import java.util.function.Supplier;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class AbstractSimpleMachineLogicModule extends AbstractLogicModule {
    private GameProfile gameProfile;
    private Supplier<Integer> machineSizeGetter;

    public void setup(IVMTileMachine machine) {
        BlockPos inventoryPos = machine.getPos().offset(Direction.UP);

        this.setEnergySourcePos(machine, inventoryPos);
        this.setInputStoragePos(machine, inventoryPos);
        this.setOutputStoragePos(machine, inventoryPos);
    }

    protected void setupInternals(String fakePlayerTypeName, Supplier<Integer> machineSizeGetter) {
        this.gameProfile = new GameProfile(UUID.randomUUID(), fakePlayerTypeName);
        this.machineSizeGetter = machineSizeGetter;
    }

    protected boolean checkStructure(IVMTileMachine machine) {
        return this.getInventory(machine) != null;
    }

    protected int getSize(IVMTileMachine machine) {
        return super.getSize(machine) * this.machineSizeGetter.get();
    }

    protected FakePlayer getFakePlayer(IWorld world) {
        return this.getFakePlayer(world, this.gameProfile);
    }
}
