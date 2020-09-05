package com.github.sejoslaw.vanillamagic2.common.quests.types.tileentities;

import com.github.sejoslaw.vanillamagic2.common.items.VMItemInventorySelector;
import com.github.sejoslaw.vanillamagic2.common.quests.types.items.QuestVMItem;
import com.github.sejoslaw.vanillamagic2.common.registries.ItemRegistry;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;

import java.util.Collection;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestInventoryBridge extends QuestVMItem<VMItemInventorySelector> {
    public VMItemInventorySelector getVMItem() {
        return (VMItemInventorySelector) ItemRegistry.INVENTORY_SELECTOR;
    }

    public void fillTooltip(Collection<String> lines) {
        super.fillTooltip(lines);

        TextUtils.addLine(lines, "quest.tooltip.save", TextUtils.getFormattedText("quest.inventoryBridge.desc.save"));
        TextUtils.addLine(lines, "quest.tooltip.create", TextUtils.getFormattedText("quest.inventoryBridge.desc.create"));
        TextUtils.addLine(lines, "quest.tooltip.clear", TextUtils.getFormattedText("quest.inventoryBridge.desc.clear"));
    }
}
