package com.github.sejoslaw.vanillamagic2.common.registries;

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

    public static Set<String> getPlayerQuests(String worldName, String playerName) {
        return USER_DATA
                .stream()
                .filter(data -> data.worldName.equals(worldName) && data.playerName.equals(playerName))
                .findFirst()
                .get()
                .questUniqueNames;
    }
}
