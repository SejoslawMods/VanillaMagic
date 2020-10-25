package com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.HopperTileEntity;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class PlayerInventoryAbsorberLogicModule extends AbstractSimpleMachineLogicModule {
    public boolean setup(IVMTileMachine machine) {
        super.setup(machine);
        this.setEnergyLevel(machine, Integer.MAX_VALUE);
        return true;
    }

    protected void work(IVMTileMachine machine) {
        if (this.getEnergyLevel(machine) <= 10) {
            this.setEnergyLevel(machine, Integer.MAX_VALUE);
        }

        WorldUtils.forServer(machine.getWorld(), serverWorld -> {
            ServerPlayerEntity player = serverWorld.getWorld().getServer().getPlayerList().getPlayers()
                    .stream()
                    .filter(p -> p.getGameProfile().getName().equals(this.getPlacedBy(machine)))
                    .findFirst()
                    .orElse(null);

            if (player == null || serverWorld.getWorld().getRedstonePowerFromNeighbors(machine.getPos()) > 0) {
                return;
            }

            for (int i = 9; i < 36; ++i) {
                ItemStack stack = player.inventory.mainInventory.get(i);

                if (stack.getItem() == Items.AIR) {
                    continue;
                }

                HopperTileEntity.putStackInInventoryAllSlots(null, this.getInventory(machine), stack.copy(), null);
                stack.setCount(0);
            }
        });
    }
}
