package com.github.sejoslaw.vanillamagic2.common.handlers;

import com.github.sejoslaw.vanillamagic2.common.registries.PlayerQuestProgressRegistry;
import com.github.sejoslaw.vanillamagic2.core.VMFiles;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class PlayerQuestProgressLoadHandler {
    @SubscribeEvent
    public void loadPlayerQuests(PlayerEvent.LoadFromFile event) {
        VMFiles.parsePlayerQuests(event.getPlayer(), (worldName, playerName, playerQuestsFile) ->
                VMFiles.readJson(playerQuestsFile, jsonService ->
                        PlayerQuestProgressRegistry.USER_DATA.add(new PlayerQuestProgressRegistry.PlayerQuestProgressData(worldName, playerName, jsonService.getEntries()))));
    }
}
