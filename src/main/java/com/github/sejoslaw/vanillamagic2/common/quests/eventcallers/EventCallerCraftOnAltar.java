package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestCraftOnAltar;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerCraftOnAltar extends EventCallerCraftable<QuestCraftOnAltar> {
    public void fillRecipes() {
        this.quests.forEach(quest -> this.recipes.put(quest.ingredients, quest.results));
    }
}
