package com.github.sejoslaw.vanillamagic2.core;

import com.github.sejoslaw.vanillamagic2.common.registries.*;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class VMRegistries {
    public static void initialize() {
        ItemRegistry.initialize();
        MachineLogicRegistry.initialize();
        EvokerSpellRegistry.initialize();
        ItemUpgradeRegistry.initialize();
        ItemTierRegistry.initialize();
        SummonEntityLogicRegistry.initialize();
        SpellRegistry.initialize();
        QuestRegistry.initialize();
    }
}
