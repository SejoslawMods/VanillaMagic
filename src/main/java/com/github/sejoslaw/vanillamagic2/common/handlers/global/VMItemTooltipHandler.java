package com.github.sejoslaw.vanillamagic2.common.handlers.global;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.handlers.EventHandler;
import com.github.sejoslaw.vanillamagic2.common.registries.ItemRegistry;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VMItemTooltipHandler extends EventHandler {
    @SubscribeEvent
    public void showTooltip(ItemTooltipEvent event) {
        this.onShowTooltip(event, VMForgeConfig.SHOW_VMITEM_TOOLTIPS.get(), (player, stack, tooltips) ->
                ItemRegistry.forEachRegistered(vmItem -> {
                    if (vmItem.isVMItem(stack)) {
                        vmItem.addTooltip(stack, tooltips);
                    }
                }));
    }
}
