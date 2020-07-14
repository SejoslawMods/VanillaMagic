package com.github.sejoslaw.vanillamagic2.common.quests;

import com.github.sejoslaw.vanillamagic2.common.json.IJsonService;
import net.minecraftforge.common.MinecraftForge;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestEventCaller {
    public final String key;
    public final Class<? extends EventCaller<? extends Quest>> eventCallerClass;
    public final Class<? extends Quest> questClass;

    private EventCaller<? extends Quest> eventCaller;

    public QuestEventCaller(String key, Class<? extends EventCaller<? extends Quest>> eventCallerClass, Class<? extends Quest> questClass) {
        this.key = key;
        this.eventCallerClass = eventCallerClass;
        this.questClass = questClass;
    }

    public QuestEventCaller register() {
        try {
            this.eventCaller = this.eventCallerClass.newInstance();
            MinecraftForge.EVENT_BUS.register(this.eventCaller);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return this;
    }

    public EventCaller<? extends Quest> getEventCaller() {
        return this.eventCaller;
    }

    public void addNewQuest(IJsonService jsonService) {
        try {
            Quest quest = this.questClass.newInstance();
            quest.readData(jsonService);
            this.eventCaller.addQuest(quest);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
