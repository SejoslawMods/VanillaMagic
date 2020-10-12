package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.killers;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.AbstractLogicModule;
import com.mojang.authlib.GameProfile;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.util.FakePlayer;

import java.util.UUID;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class AbstractKillerLogicModule extends AbstractLogicModule {
    private static final String NBT_MODULE_SWORD_SLOT_ID = "NBT_MODULE_SWORD_SLOT_ID";
    private static GameProfile KILLER_GAME_PROFILE = new GameProfile(UUID.randomUUID(), "VM Killer");

    public void setup(IVMTileMachine machine) {
        BlockPos inventoryPos = machine.getPos().offset(Direction.UP);

        this.setEnergySourcePos(machine, inventoryPos);
        this.setInputStoragePos(machine, inventoryPos);
        this.setOutputStoragePos(machine, inventoryPos);
    }

    protected boolean checkStructure(IVMTileMachine machine) {
        return this.getInventory(machine) != null;
    }

    protected void setSwordSlotId(IVMTileMachine machine, int slotId) {
        this.setInt(machine, slotId, NBT_MODULE_SWORD_SLOT_ID);
    }

    protected int getSwordSlotId(IVMTileMachine machine) {
        return this.getInt(machine, NBT_MODULE_SWORD_SLOT_ID);
    }

    protected boolean isSword(ItemStack stack) {
        return stack.getItem() instanceof SwordItem || stack.getItem().getRegistryName().toString().toLowerCase().contains("_sword");
    }

    protected int getSize(IVMTileMachine machine) {
        return super.getSize(machine) * VMForgeConfig.KILLER_SIZE.get();
    }

    protected FakePlayer getKiller(IWorld world) {
        return this.getFakePlayer(world, KILLER_GAME_PROFILE);
    }
}
