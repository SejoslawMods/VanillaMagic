package com.github.sejoslaw.vanillamagic2.common.quests.types.items;

import com.github.sejoslaw.vanillamagic2.common.items.VMItemAccelerationCrystal;
import com.github.sejoslaw.vanillamagic2.common.registries.ItemRegistry;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestAccelerationCrystal extends QuestVMItem<VMItemAccelerationCrystal> {
    public VMItemAccelerationCrystal getVMItem() {
        return (VMItemAccelerationCrystal) ItemRegistry.ACCELERATION_CRYSTAL;
    }
}
