package com.github.sejoslaw.vanillamagic2.common.registries;

import com.github.sejoslaw.vanillamagic2.common.items.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ItemRegistry {
    private static final Map<String, IVMItem> ITEMS = new HashMap<>();

    public static final IVMItem ACCELERATION_CRYSTAL;
    public static final IVMItem EVOKER_CRYSTAL;
    public static final IVMItem LIQUID_SUPPRESSION_CRYSTAL;
    public static final IVMItem MOTHER_NATURE_CRYSTAL;
    public static final IVMItem INVENTORY_SELECTOR;

    static {
        ACCELERATION_CRYSTAL = register("acceleration_crystal", new VMItemAccelerationCrystal());
        EVOKER_CRYSTAL = register("evoker_crystal", new VMItemEvokerCrystal());
        LIQUID_SUPPRESSION_CRYSTAL = register("liquid_suppression_crystal", new VMItemLiquidSuppressionCrystal());
        MOTHER_NATURE_CRYSTAL = register("mother_nature_crystal", new VMItemMotherNatureCrystal());
        INVENTORY_SELECTOR = register("inventory_selector", new VMItemInventorySelector());
    }

    public static void initialize() {
    }

    private static IVMItem register(String key, IVMItem item) {
        ITEMS.put(key, item);
        return item;
    }
}
