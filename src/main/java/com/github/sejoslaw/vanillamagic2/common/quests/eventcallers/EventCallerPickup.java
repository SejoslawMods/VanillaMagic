package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestPickup;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerPickup extends EventCaller<QuestPickup> {
    @SubscribeEvent
    public void onItemPickup(PlayerEvent.ItemPickupEvent event) {
        this.executor.onItemPickup(event,
                (playerEntity, world, itemEntity, stack) -> this.quests
                        .stream()
                        .filter(quest -> quest.whatToPickStack.getItem() == stack.getItem())
                        .findFirst()
                        .orElse(null),
                (playerEntity, world, itemEntity, itemStack, quest) -> { });
    }
}
