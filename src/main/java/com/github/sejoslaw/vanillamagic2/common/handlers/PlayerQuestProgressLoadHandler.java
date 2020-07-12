package com.github.sejoslaw.vanillamagic2.common.handlers;

import com.github.sejoslaw.vanillamagic2.common.registries.PlayerQuestProgressRegistry;
import com.github.sejoslaw.vanillamagic2.core.VMFiles;
import com.google.gson.JsonObject;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class PlayerQuestProgressLoadHandler {
    @SubscribeEvent
    public void loadPlayerQuests(PlayerEvent.LoadFromFile event) {
        VMFiles.parsePlayerQuests(event.getPlayer(), (worldName, playerName, playerQuestsFile) -> {
            VMFiles.readJson(playerQuestsFile, rootElement -> {
                JsonObject jo = rootElement.getAsJsonObject();
                Set<String> questUniqueNames = jo.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toSet());
                PlayerQuestProgressRegistry.USER_DATA.add(new PlayerQuestProgressRegistry.PlayerQuestProgressData(worldName, playerName, questUniqueNames));
            });
        });
    }
}
