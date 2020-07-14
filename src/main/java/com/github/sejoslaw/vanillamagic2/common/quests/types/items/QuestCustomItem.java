package com.github.sejoslaw.vanillamagic2.common.quests.types.items;

import com.github.sejoslaw.vanillamagic2.common.items.ICustomItem;
import com.github.sejoslaw.vanillamagic2.common.quests.Quest;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class QuestCustomItem<TCustomItem extends ICustomItem> extends Quest {
    /**
     * @return New instance of Custom Item definition.
     */
    public abstract TCustomItem getCustomItem();
}
