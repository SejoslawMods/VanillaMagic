package com.github.sejoslaw.vanillamagic2.common.quests.eventCallers.items;

import com.github.sejoslaw.vanillamagic2.common.items.ICustomItem;
import com.github.sejoslaw.vanillamagic2.common.quests.eventCallers.EventCallerCraftable;
import com.github.sejoslaw.vanillamagic2.common.quests.types.items.QuestCustomItem;

import java.util.Collections;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class EventCallerCustomItem<TQuest extends QuestCustomItem<? extends ICustomItem>> extends EventCallerCraftable<TQuest> {
    public void fillRecipes() {
        ICustomItem customItem = this.getCustomItem();
        this.recipes.put(customItem.getIngredients(), Collections.singletonList(customItem.getStack()));
    }

    protected ICustomItem getCustomItem() {
        return this.quests.get(0).getCustomItem();
    }
}
