package com.github.sejoslaw.vanillamagic.api.quest;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class which stores all registered Quests.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestRegistry {
    /**
     * All registered Quests
     */
    private static List<IQuest> QUESTS = new ArrayList<>();

    /**
     * Map: <br>
     * Key - Quest Unique Name <br>
     * Value - Quest <br>
     */
    private static Map<String, IQuest> QUESTS_MAP = new HashMap<>();

    private QuestRegistry() {
    }

    /**
     * Main method for adding Quests.
     */
    public static void addQuest(IQuest q) {
        QuestRegistry.QUESTS.add(q);
        QuestRegistry.QUESTS_MAP.put(q.getUniqueName(), q);
    }

    /**
     * @return Returns Quest from the Map, where "key" is a uniqueName of the Quest.
     */
    @Nullable
    public static IQuest get(String key) {
        return QUESTS_MAP.get(key);
    }

    /**
     * @return Returns Quest from the List.
     */
    public static IQuest get(int index) {
        return QUESTS.get(index);
    }

    /**
     * @return Returns Quest List size.
     */
    public static int size() {
        return QUESTS.size();
    }

    /**
     * @return Returns Quests as List.
     */
    public static List<IQuest> getQuests() {
        return QUESTS;
    }
}