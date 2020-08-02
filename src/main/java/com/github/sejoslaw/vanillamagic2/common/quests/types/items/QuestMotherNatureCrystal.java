package com.github.sejoslaw.vanillamagic2.common.quests.types.items;

import com.github.sejoslaw.vanillamagic2.common.items.VMItemMotherNatureCrystal;
import com.github.sejoslaw.vanillamagic2.common.registries.ItemRegistry;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestMotherNatureCrystal extends QuestVMItem<VMItemMotherNatureCrystal> {
    public VMItemMotherNatureCrystal getVMItem() {
        return (VMItemMotherNatureCrystal) ItemRegistry.MOTHER_NATURE_CRYSTAL;
    }
}
