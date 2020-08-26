package com.github.sejoslaw.vanillamagic2.common.handlers.core;

import com.github.sejoslaw.vanillamagic2.common.json.JsonService;
import com.github.sejoslaw.vanillamagic2.common.registries.PlayerQuestProgressRegistry;
import com.github.sejoslaw.vanillamagic2.core.VMFiles;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Set;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class PlayerQuestProgressSaveHandler {
    @SubscribeEvent
    public void savePlayerQuests(PlayerEvent.PlayerLoggedOutEvent event) {
        VMFiles.parsePlayerQuests(event.getPlayer(), (worldName, playerName, playerQuestsFile) ->
                VMFiles.writeJson(playerQuestsFile, writer -> {
                    Set<String> playerQuests = PlayerQuestProgressRegistry.getPlayerQuests(playerName);
                    JsonService.writePlayerQuestProgress(writer, playerQuests);
                    PlayerQuestProgressRegistry.clearData(playerName);
                }));
    }
}
