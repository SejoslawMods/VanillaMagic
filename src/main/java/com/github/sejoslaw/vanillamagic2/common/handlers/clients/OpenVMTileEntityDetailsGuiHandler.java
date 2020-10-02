package com.github.sejoslaw.vanillamagic2.common.handlers.clients;

import com.github.sejoslaw.vanillamagic2.common.guis.IGuiProvider;
import com.github.sejoslaw.vanillamagic2.common.guis.VMGui;
import com.github.sejoslaw.vanillamagic2.common.guis.VMTileEntityDetailsGui;
import com.github.sejoslaw.vanillamagic2.common.handlers.EventHandler;
import com.github.sejoslaw.vanillamagic2.common.tileentities.IVMTileEntity;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
@OnlyIn(Dist.CLIENT)
public class OpenVMTileEntityDetailsGuiHandler extends EventHandler {
    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        this.onPlayerInteract(event, (player, world, pos, direction) -> {
            if (Minecraft.getInstance().currentScreen != null) {
                return;
            }

            IVMTileEntity tile = WorldUtils.getVMTile(world, pos);

            if (tile == null) {
                return;
            }

            Screen gui = null;

            if (tile instanceof IGuiProvider) {
                gui = ((IGuiProvider) tile).getGui();
            }

            if (gui == null) {
                gui = new VMTileEntityDetailsGui(tile);
            }

            VMGui.displayGui(gui);
        });
    }
}
