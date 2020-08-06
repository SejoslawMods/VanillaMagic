package com.github.sejoslaw.vanillamagic2.common.registries;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IMachineModule;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.*;

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
        DEFAULT_MODULES.add(new WorkingPositionModule());
        DEFAULT_MODULES.add(new StorageModule());
        DEFAULT_MODULES.add(new SmeltableTicksEnergyModule());

        registerModule(QUARRY_KEY, new QuarryLogicModule()); // TODO: extends AbstractLogicModule
        registerModule(FARM_KEY, new FarmLogicModule());
    }

    private static void registerModule(String key, IMachineModule module) {
        if (!MODULES.containsKey(key)) {
            MODULES.put(key, new ArrayList<>());
        }

        MODULES.get(key).add(module);
    }
}
