package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules;

import com.github.sejoslaw.vanillamagic2.common.functions.Consumer3;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import com.mojang.authlib.GameProfile;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
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
    private static final String NBT_MODULE_SLOT_ID = "NBT_MODULE_SLOT_ID";

    private GameProfile gameProfile;
    private Supplier<Integer> machineSizeGetter;

    public void setup(IVMTileMachine machine) {
        BlockPos inventoryPos = machine.getPos().offset(Direction.UP);

        this.setEnergySourcePos(machine, inventoryPos);
        this.setInputStoragePos(machine, inventoryPos);
        this.setOutputStoragePos(machine, inventoryPos);

        this.setSlotId(machine, 0);
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

    protected int getSlotId(IVMTileMachine machine) {
        return this.getInt(machine, NBT_MODULE_SLOT_ID);
    }

    protected void setSlotId(IVMTileMachine machine, int slotId) {
        this.setInt(machine, slotId, NBT_MODULE_SLOT_ID);
    }

    protected void useSlot(IVMTileMachine machine, Consumer3<IInventory, Integer, ItemStack> consumer) {
        int slotId = this.getSlotId(machine);
        IInventory inv = this.getInventory(machine);
        ItemStack stack = inv.getStackInSlot(slotId);

        consumer.accept(inv, slotId, stack);
    }
}
