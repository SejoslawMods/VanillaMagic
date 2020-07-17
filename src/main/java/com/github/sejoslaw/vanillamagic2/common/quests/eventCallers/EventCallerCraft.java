package com.github.sejoslaw.vanillamagic2.common.quests.eventCallers;

import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestCraft;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerCraft extends EventCaller<QuestCraft> {
    @SubscribeEvent
    public void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        this.executor.onItemCrafted(event,
                (player, result, inv) ->
                        this.quests
                            .stream()
                            .filter(quest -> result.getItem() == quest.iconStack.getItem())
                            .findFirst()
                            .orElse(null),
                (player, result, inv) -> { });
    }
}
