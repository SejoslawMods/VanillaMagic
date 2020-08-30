package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestCraftOnAltar;
import com.github.sejoslaw.vanillamagic2.common.recipes.AltarRecipe;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerCraftOnAltar extends EventCallerCraftable<QuestCraftOnAltar> {
    public void fillRecipes() {
        this.quests.forEach(quest -> this.recipes.add(new AltarRecipe(quest, quest.ingredients, quest.results)));
    }
}
