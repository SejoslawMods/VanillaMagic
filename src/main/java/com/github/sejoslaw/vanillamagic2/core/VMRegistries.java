package com.github.sejoslaw.vanillamagic2.core;

import com.github.sejoslaw.vanillamagic2.common.registries.ItemTierRegistry;
import com.github.sejoslaw.vanillamagic2.common.registries.ItemUpgradeRegistry;
import com.github.sejoslaw.vanillamagic2.common.registries.QuestRegistry;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class VMRegistries {
    public static void initialize() {
        ItemUpgradeRegistry.initialize();
        ItemTierRegistry.initialize();
        QuestRegistry.initialize();
    }
}
