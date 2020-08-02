package com.github.sejoslaw.vanillamagic2.common.quests.types.items;

import com.github.sejoslaw.vanillamagic2.common.items.VMItemEvokerCrystal;
import com.github.sejoslaw.vanillamagic2.common.registries.ItemRegistry;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestEvokerCrystal extends QuestVMItem<VMItemEvokerCrystal> {
    public VMItemEvokerCrystal getVMItem() {
        return (VMItemEvokerCrystal) ItemRegistry.EVOKER_CRYSTAL;
    }
}
