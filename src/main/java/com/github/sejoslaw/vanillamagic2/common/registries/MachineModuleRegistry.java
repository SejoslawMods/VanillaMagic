package com.github.sejoslaw.vanillamagic2.common.registries;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IMachineModule;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.*;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.farms.BoneMealApplyingFarmLogicModule;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.farms.TreeCuttingFarmLogicModule;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.farms.PlantingFarmLogicModule;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.farms.WorkingPosMoverFarmLogicModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class MachineModuleRegistry {
    public static final List<IMachineModule> DEFAULT_MODULES = new ArrayList<>();
    public static final Map<String, List<IMachineModule>> MODULES = new HashMap<>();

    public static final String QUARRY_KEY = "quarry";
    public static final String FARM_KEY = "farm";

    public static void initialize() {
        DEFAULT_MODULES.add(new SmeltableTicksEnergyModule());
        DEFAULT_MODULES.add(new ForgeEnergyModule());
        DEFAULT_MODULES.add(new ForgeCapabilityEnergyModule());

        // Quarry
        registerModule(QUARRY_KEY, new QuarryLogicModule());

        // Farm
        registerModule(FARM_KEY, new WorkingPosMoverFarmLogicModule());
        registerModule(FARM_KEY, new PlantingFarmLogicModule());
        registerModule(FARM_KEY, new BoneMealApplyingFarmLogicModule());
        registerModule(FARM_KEY, new TreeCuttingFarmLogicModule());
    }

    private static void registerModule(String key, IMachineModule module) {
        if (!MODULES.containsKey(key)) {
            MODULES.put(key, new ArrayList<>());
        }

        MODULES.get(key).add(module);
    }
}
