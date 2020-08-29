package com.github.sejoslaw.vanillamagic2.common.quests.types.items;

import com.github.sejoslaw.vanillamagic2.common.items.IVMItem;
import com.github.sejoslaw.vanillamagic2.common.quests.Quest;

import java.util.Collection;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class QuestVMItem<TVMItem extends IVMItem> extends Quest {
    /**
     * @return New instance of VM Item definition.
     */
    public abstract TVMItem getVMItem();

    public void fillTooltip(Collection<String> lines) {
        super.fillTooltip(lines);

        IVMItem vmItem = this.getVMItem();
        this.addLine(lines, "quest.tooltip.ingredients", this.getTooltip(vmItem.getIngredients()));
        this.addLine(lines, "quest.tooltip.results", this.getTooltip(vmItem.getStack()));
    }
}
