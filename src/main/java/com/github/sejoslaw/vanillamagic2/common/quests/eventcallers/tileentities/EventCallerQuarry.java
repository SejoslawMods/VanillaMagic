package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers.tileentities;

import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.types.tileentities.QuestQuarry;
import com.github.sejoslaw.vanillamagic2.common.registries.MachineModuleRegistry;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerQuarry extends EventCaller<QuestQuarry> {
    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        this.executor.addVMTileMachine(event, MachineModuleRegistry.QUARRY_KEY);
    }
}
