package com.github.sejoslaw.vanillamagic2.common.registries;

import com.github.sejoslaw.vanillamagic2.common.spells.summon.logics.*;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.IWorld;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class SummonEntityLogicRegistry {
    private static final Set<SummonEntityLogic> LOGICS = new HashSet<>();

    public static void initialize() {
        // Overworld Monsters
        LOGICS.add(new SkeletonSummonLogic());
        LOGICS.add(new SpiderSummonLogic());
        LOGICS.add(new SlimeSummonLogic());
        LOGICS.add(new GuardianSummonLogic());
        LOGICS.add(new VexSummonLogic());
        LOGICS.add(new StraySummonLogic());

        // Overworld Entities
        LOGICS.add(new VillagerSummonLogic());

        // Nether Entities
        LOGICS.add(new PiglinSummonLogic());

        // End Entities
        LOGICS.add(new EndermiteSummonLogic());
    }

    public static Entity getEntity(IWorld world, EntityType<? extends Entity> defaultType) {
        SummonEntityLogic logic = find(defaultType);
        return logic != null ? logic.getEntity(world) : defaultType.create(WorldUtils.asWorld(world));
    }

    public static Entity getHorse(IWorld world, EntityType<? extends Entity> defaultType) {
        SummonEntityLogic logic = find(defaultType);
        return logic != null ? logic.getHorse(world) : null;
    }

    public static double getPercent(EntityType<? extends Entity> defaultType) {
        SummonEntityLogic logic = find(defaultType);
        return logic != null ? logic.getPercent() : 0;
    }

    private static SummonEntityLogic find(EntityType<? extends Entity> defaultType) {
        return LOGICS.stream().filter(logic -> logic.entityType == defaultType).findFirst().orElse(null);
    }
}
