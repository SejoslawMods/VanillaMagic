package com.github.sejoslaw.vanillamagic2.common.registries;

import com.github.sejoslaw.vanillamagic2.common.quests.Quest;
import com.github.sejoslaw.vanillamagic2.common.quests.QuestEventCaller;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class QuestRegistry {
    private static final List<QuestEventCaller> QUEST_EVENT_CALLERS = new ArrayList<>();

    public static void initialize() {
        QUEST_EVENT_CALLERS.add(new QuestEventCaller("craftOnAltar", EventCallerCraftOnAltar.class, QuestCraftOnAltar.class).register());
    }

    public static void readQuest(JsonObject jo) {
        try {
            String questEventCallerKey = jo.get("eventCaller").getAsString();
            QuestEventCaller caller = QUEST_EVENT_CALLERS.stream().filter(c -> c.key.equals(questEventCallerKey)).findFirst().get();
            caller.addNewQuest(jo);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Quest getQuest(String questUniqueName) {
        for (QuestEventCaller caller : QUEST_EVENT_CALLERS) {
            for (Quest quest : caller.getEventCaller().quests) {
                if (quest.uniqueName.equals(questUniqueName)) {
                    return quest;
                }
            }
        }

        return null;
    }
}
