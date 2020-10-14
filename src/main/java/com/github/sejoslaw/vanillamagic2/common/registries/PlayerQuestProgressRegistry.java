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
        public final String playerName;
        public final Set<String> questUniqueNames;

        public PlayerQuestProgressData(String playerName, Set<String> questUniqueNames) {
            this.playerName = playerName;
            this.questUniqueNames = questUniqueNames;
        }
    }

    /**
     * Contains data about Player Quests read from file.
     */
    private static final Set<PlayerQuestProgressData> USER_DATA = new HashSet<>();

    public static PlayerQuestProgressData getPlayerData(PlayerEntity player) {
        String playerName = EntityUtils.getPlayerNameFormatted(player);
        return getPlayerData(playerName);
    }

    public static PlayerQuestProgressData getPlayerData(String playerName) {
        return USER_DATA
                .stream()
                .filter(data -> data.playerName.equals(playerName))
                .findFirst()
                .orElse(new PlayerQuestProgressData(playerName, new HashSet<>()));
    }

    public static Set<String> getPlayerQuests(PlayerEntity player) {
        return getPlayerData(player).questUniqueNames;
    }

    public static Set<String> getPlayerQuests(String playerName) {
        return getPlayerData(playerName).questUniqueNames;
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

    public static void clearData(String playerName) {
        USER_DATA.remove(getPlayerData(playerName));
    }

    public static void setupPlayerData(String playerName, Set<String> questNames) {
        USER_DATA.add(new PlayerQuestProgressRegistry.PlayerQuestProgressData(playerName, questNames));
    }
}
