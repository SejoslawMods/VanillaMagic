package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers.items;

import com.github.sejoslaw.vanillamagic2.common.items.IVMItem;
import com.github.sejoslaw.vanillamagic2.common.quests.eventcallers.EventCallerCraftable;
import com.github.sejoslaw.vanillamagic2.common.quests.types.items.QuestVMItem;

import java.util.Collections;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class EventCallerVMItem<TQuest extends QuestVMItem<? extends IVMItem>> extends EventCallerCraftable<TQuest> {
    public void fillRecipes() {
        IVMItem vmItem = this.getVMItem();
        this.recipes.put(vmItem.getIngredients(), Collections.singletonList(vmItem.getStack()));
    }

    protected IVMItem getVMItem() {
        return this.quests.get(0).getVMItem();
    }
}
