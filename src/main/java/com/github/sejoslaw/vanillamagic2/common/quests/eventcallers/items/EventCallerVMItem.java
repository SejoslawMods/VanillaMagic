package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers.items;

import com.github.sejoslaw.vanillamagic2.common.items.IVMItem;
import com.github.sejoslaw.vanillamagic2.common.quests.eventcallers.EventCallerCraftable;
import com.github.sejoslaw.vanillamagic2.common.quests.types.items.QuestVMItem;
import com.github.sejoslaw.vanillamagic2.common.recipes.AltarRecipe;

import java.util.Collections;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class EventCallerVMItem<TQuest extends QuestVMItem<? extends IVMItem>> extends EventCallerCraftable<TQuest> {
    public void fillRecipes() {
        IVMItem vmItem = this.getVMItem();
        TQuest quest = this.quests.get(0);
        this.recipes.add(new AltarRecipe(quest, quest.ingredients, Collections.singletonList(vmItem.getStack())));
    }

    protected IVMItem getVMItem() {
        return this.quests.get(0).getVMItem();
    }
}
