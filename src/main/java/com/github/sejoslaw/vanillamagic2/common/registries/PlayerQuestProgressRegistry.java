package com.github.sejoslaw.vanillamagic2.common.registries;

import com.github.sejoslaw.vanillamagic2.common.quests.Quest;
import com.github.sejoslaw.vanillamagic2.common.utils.EntityUtils;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class PlayerQuestProgressRegistry {
    public static final class PlayerQuestProgressData {
        public final String worldName;
        public final String playerName;
        public final Set<String> questUniqueNames;

        public PlayerQuestProgressData(String worldName, String playerName, Set<String> questUniqueNames) {
            this.worldName = worldName;
            this.playerName = playerName;
            this.questUniqueNames = questUniqueNames;
        }
    }

    /**
     * Contains data about Player Quests read from file.
     */
    public static final Set<PlayerQuestProgressData> USER_DATA = new HashSet<>();

    public static PlayerQuestProgressData getPlayerData(PlayerEntity player) {
        String worldName = player.getEntityWorld().getWorldInfo().getWorldName();
        String playerName = EntityUtils.getPlayerName(player);
        return getPlayerData(worldName, playerName);
    }

    public static PlayerQuestProgressData getPlayerData(String worldName, String playerName) {
        return USER_DATA
                .stream()
                .filter(data -> data.worldName.equals(worldName) && data.playerName.equals(playerName))
                .findFirst()
                .orElse(new PlayerQuestProgressData(worldName, playerName, new HashSet<>()));
    }

    public static Set<String> getPlayerQuests(String worldName, String playerName) {
        return getPlayerData(worldName, playerName).questUniqueNames;
    }

    public static boolean hasPlayerGotQuest(PlayerEntity player, String questUniqueName) {
        return getPlayerData(player)
                .questUniqueNames
                .stream()
                .anyMatch(name -> name.equals(questUniqueName));
    }

    public static boolean canPlayerGetQuest(PlayerEntity player, String questUniqueName) {
        Quest quest = QuestRegistry.getQuest(questUniqueName);

        if (quest.parent != null) {
            return hasPlayerGotQuest(player, quest.parent.uniqueName);
        }

        return true;
    }

    public static void givePlayerQuest(PlayerEntity player, String questUniqueName) {
        getPlayerData(player).questUniqueNames.add(questUniqueName);
    }
}
