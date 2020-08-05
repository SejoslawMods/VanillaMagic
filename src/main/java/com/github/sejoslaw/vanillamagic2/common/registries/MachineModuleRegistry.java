package com.github.sejoslaw.vanillamagic2.common.registries;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IMachineModule;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        DEFAULT_MODULES.add(new SmeltableTicksEnergyModule());

        registerModule(QUARRY_KEY, new QuarryLogicModule());
        registerModule(FARM_KEY, new FarmLogicModule());
    }

    public static <TModule extends IMachineModule> TModule getModule(Class<TModule> clazz) {
        IMachineModule module = DEFAULT_MODULES.stream().filter(m -> m.getClass() == clazz).findFirst().orElse(null);

        if (module != null) {
            return (TModule) module;
        }

        for (List<IMachineModule> list : MODULES.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList())) {
            module = list.stream().filter(m -> m.getClass() == clazz).findFirst().orElse(null);

            if (module != null) {
                return (TModule) module;
            }
        }


        return null;
    }

    private static void registerModule(String key, IMachineModule module) {
        if (!MODULES.containsKey(key)) {
            MODULES.put(key, new ArrayList<>());
        }

        MODULES.get(key).add(module);
    }
}
