package com.github.sejoslaw.vanillamagic2.common.registries;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IMachineModule;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.*;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.farms.*;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.killers.*;

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
    public static final String KILLER_KEY = "killer";
    public static final String BREEDER_KEY = "breeder";

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
        registerModule(FARM_KEY, new FarmingFarmLogicModule());
        registerModule(FARM_KEY, new ItemCollectingFarmLogicModule());

        // Killer
        registerModule(KILLER_KEY, new SwordFindingKillerLogicModule());
        registerModule(KILLER_KEY, new AttackingKillerLogicModule());

        // Breeder
        // -- iterate over items module
        // -- try apply item module
    }

    private static void registerModule(String key, IMachineModule module) {
        if (!MODULES.containsKey(key)) {
            MODULES.put(key, new ArrayList<>());
        }

        MODULES.get(key).add(module);
    }
}
