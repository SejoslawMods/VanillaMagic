package com.github.sejoslaw.vanillamagic2.common.quests.types.tileentities;

import com.github.sejoslaw.vanillamagic2.common.items.VMItemInventorySelector;
import com.github.sejoslaw.vanillamagic2.common.quests.types.items.QuestVMItem;
import com.github.sejoslaw.vanillamagic2.common.registries.ItemRegistry;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestInventoryBridge extends QuestVMItem<VMItemInventorySelector> {
    public VMItemInventorySelector getVMItem() {
        return (VMItemInventorySelector) ItemRegistry.INVENTORY_SELECTOR;
    }
}
