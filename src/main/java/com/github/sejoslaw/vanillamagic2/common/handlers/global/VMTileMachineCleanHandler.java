package com.github.sejoslaw.vanillamagic2.common.handlers.global;

import com.github.sejoslaw.vanillamagic2.common.handlers.EventHandler;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IVMTileMachine;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VMTileMachineCleanHandler extends EventHandler {
    @SubscribeEvent
    public void cleanMachines(BlockEvent.BreakEvent event) {
        this.onBlockBreak(event, (player, world, pos, state) -> {
            WorldUtils.asWorld(world).tickableTileEntities
                    .stream()
                    .filter(tile -> tile instanceof IVMTileMachine && tile.getPos().equals(pos))
                    .forEach(tile -> ((IVMTileMachine) tile).removeTileEntity());
        });
    }
}
