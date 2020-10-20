package com.github.sejoslaw.vanillamagic2.common.handlers.global;

import com.github.sejoslaw.vanillamagic2.common.handlers.EventHandler;
import com.github.sejoslaw.vanillamagic2.common.registries.VMNetworkRegistry;
import com.github.sejoslaw.vanillamagic2.common.tileentities.IVMTileEntity;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class OpenVMTileEntityDetailsGuiHandler extends EventHandler {
    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        this.onPlayerInteract(event, (player, world, pos, direction) -> {
            IVMTileEntity tile = WorldUtils.getVMTile(world, pos);

            if (tile == null) {
                return;
            }

            VMNetworkRegistry.openVMTileEntityDetailsGui(player, tile);
        });
    }
}
