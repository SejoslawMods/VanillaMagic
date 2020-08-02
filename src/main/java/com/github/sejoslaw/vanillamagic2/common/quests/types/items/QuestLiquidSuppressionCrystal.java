package com.github.sejoslaw.vanillamagic2.common.quests.types.items;

import com.github.sejoslaw.vanillamagic2.common.items.VMItemLiquidSuppressionCrystal;
import com.github.sejoslaw.vanillamagic2.common.registries.ItemRegistry;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestLiquidSuppressionCrystal extends QuestVMItem<VMItemLiquidSuppressionCrystal> {
    public VMItemLiquidSuppressionCrystal getVMItem() {
        return (VMItemLiquidSuppressionCrystal) ItemRegistry.LIQUID_SUPPRESSION_CRYSTAL;
    }
}
