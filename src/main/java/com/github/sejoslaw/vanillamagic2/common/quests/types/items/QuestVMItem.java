package com.github.sejoslaw.vanillamagic2.common.quests.types.items;

import com.github.sejoslaw.vanillamagic2.common.items.IVMItem;
import com.github.sejoslaw.vanillamagic2.common.quests.Quest;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class QuestVMItem<TVMItem extends IVMItem> extends Quest {
    /**
     * @return New instance of VM Item definition.
     */
    public abstract TVMItem getVMItem();
}
