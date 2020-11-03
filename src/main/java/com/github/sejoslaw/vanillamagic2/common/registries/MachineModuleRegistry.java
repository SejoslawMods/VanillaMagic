package com.github.sejoslaw.vanillamagic2.common.registries;

import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.IMachineModule;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.*;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.breeders.*;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.farms.*;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.modules.killers.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class MachineModuleRegistry {
    private static final List<IMachineModule> DEFAULT_MODULES = new ArrayList<>();
    private static final Map<String, List<IMachineModule>> MODULES = new HashMap<>();

    public static final String QUARRY_KEY = "quarry";
    public static final String FARM_KEY = "farm";
    public static final String KILLER_KEY = "killer";
    public static final String BREEDER_KEY = "breeder";
    public static final String PLAYER_INVENTORY_ABSORBER_KEY = "playerInventoryAbsorber";

    public static void initialize() {
        registerDefaultModule(new SmeltableTicksEnergyModule());
        registerDefaultModule(new ForgeEnergyModule());
        registerDefaultModule(new ForgeCapabilityEnergyModule());

        // Quarry
        registerModule(QUARRY_KEY, new QuarryLogicModule());

        // Farm
        registerModule(FARM_KEY, new WorkingPosMoverFarmLogicModule());
        registerModule(FARM_KEY, new PlantingFarmLogicModule());
        registerModule(FARM_KEY, new BoneMealApplyingFarmLogicModule());
        registerModule(FARM_KEY, new TreeCuttingFarmLogicModule());
        registerModule(FARM_KEY, new FarmingFarmLogicModule());
        registerModule(FARM_KEY, new ItemCollectingFarmLogicModule());
        registerModule(FARM_KEY, new HoeFindingFarmLogicModule());
        registerModule(FARM_KEY, new SoilShapingFarmLogicModule());

        // Killer
        registerModule(KILLER_KEY, new SwordFindingKillerLogicModule());
        registerModule(KILLER_KEY, new AttackingKillerLogicModule());

        // Breeder
        registerModule(BREEDER_KEY, new FindingBreederLogicModule());
        registerModule(BREEDER_KEY, new UsingItemBreederLogicModule());

        // Player Inventory Absorber
        registerModule(PLAYER_INVENTORY_ABSORBER_KEY, new PlayerInventoryAbsorberLogicModule());
    }

    /**
     * Registers new Default Module.
     */
    public static void registerDefaultModule(IMachineModule module) {
        DEFAULT_MODULES.add(module);
    }

    /**
     * Registers new Module for specified Machine.
     */
    public static void registerModule(String key, IMachineModule module) {
        if (!MODULES.containsKey(key)) {
            MODULES.put(key, new ArrayList<>());
        }

        MODULES.get(key).add(module);
    }

    /**
     * @return Stream for default Modules.
     */
    public static Stream<IMachineModule> getDefaultModulesStream() {
        return DEFAULT_MODULES.stream();
    }

    /**
     * @return Stream for Modules for specified Machine.
     */
    public static Stream<IMachineModule> getModulesStream(String key) {
        return MODULES.get(key).stream();
    }

    /**
     * Executes action for each of the default Modules.
     */
    public static void forEachDefaultModule(Consumer<IMachineModule> consumer) {
        getDefaultModulesStream().forEach(consumer);
    }

    /**
     * Exexcutes action for each of the Modules for selected Machine.
     */
    public static void forEachModules(String key, Consumer<IMachineModule> consumer) {
        getModulesStream(key).forEach(consumer);
    }
}
