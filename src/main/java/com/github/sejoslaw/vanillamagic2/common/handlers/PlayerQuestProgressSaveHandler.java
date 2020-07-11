package com.github.sejoslaw.vanillamagic2.common.handlers;

import com.github.sejoslaw.vanillamagic2.common.registries.QuestRegistry;
import com.github.sejoslaw.vanillamagic2.core.VMFiles;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class PlayerQuestProgressSaveHandler {
    @SubscribeEvent
    public void savePlayerQuests(PlayerEvent.SaveToFile event) {
        VMFiles.parsePlayerQuests(event.getPlayer(), (worldName, playerName, playerQuestsFile) -> {
            VMFiles.writeJson(playerQuestsFile, jsonWriter -> {
                try {
                    jsonWriter.beginObject();

                    for (String questUniqueName : QuestRegistry.getPlayerQuests(worldName, playerName)) {
                        jsonWriter.name(questUniqueName).value(1);
                    }

                    jsonWriter.endObject();
                    jsonWriter.flush();
                    jsonWriter.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
        });
    }
}
