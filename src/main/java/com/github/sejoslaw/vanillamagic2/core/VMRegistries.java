package com.github.sejoslaw.vanillamagic2.core;

import com.github.sejoslaw.vanillamagic2.common.registries.ItemTierRegistry;
import com.github.sejoslaw.vanillamagic2.common.registries.ItemUpgradeRegistry;
import com.github.sejoslaw.vanillamagic2.common.registries.QuestRegistry;
import com.github.sejoslaw.vanillamagic2.common.registries.SpellRegistry;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class VMRegistries {
    public static void initialize() {
        ItemUpgradeRegistry.initialize();
        ItemTierRegistry.initialize();
        SpellRegistry.initialize();
        QuestRegistry.initialize();
    }
}
