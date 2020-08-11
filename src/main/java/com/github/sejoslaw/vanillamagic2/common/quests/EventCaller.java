package com.github.sejoslaw.vanillamagic2.common.quests;

import com.github.sejoslaw.vanillamagic2.core.VMEvents;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCaller<TQuest extends Quest> {
    public final List<TQuest> quests = new ArrayList<>();
    public final EventExecutor<TQuest> executor = new EventExecutor<>(this);

    public void addQuest(Quest quest) {
        quests.add((TQuest) quest);
    }

    public void register() {
        VMEvents.register(this);
    }
}
