package com.github.sejoslaw.vanillamagic2.common.registries;

import com.github.sejoslaw.vanillamagic2.common.spells.summon.SpellSummonEntity;
import com.github.sejoslaw.vanillamagic2.common.spells.summon.logics.*;
import net.minecraft.entity.EntityType;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class SpellSummonEntityRegistry {
    private static final Set<SpellSummonEntity<?>> SPELLS = new HashSet<>();

    public static void initialize() {
        // Overworld Monsters
        SPELLS.add(new SkeletonSummonSpell());
        SPELLS.add(new SpiderSummonSpell());
        SPELLS.add(new SlimeSummonSpell());
        SPELLS.add(new GuardianSummonSpell());
        SPELLS.add(new VexSummonSpell());
        SPELLS.add(new StraySummonSpell());

        // Overworld Entities
        SPELLS.add(new VillagerSummonSpell());

        // Nether Entities
        SPELLS.add(new PiglinSummonSpell());
        SPELLS.add(new ZombifiedPiglinSummonSpell());
        SPELLS.add(new PiglinBruteSummonSpell());
        SPELLS.add(new MagmaCubeSummonSpell());
        SPELLS.add(new WitherSkeletonSummonSpell());

        // End Entities
        SPELLS.add(new EndermiteSummonSpell());
    }

    public static SpellSummonEntity<?> getLogic(EntityType<?> entityType) {
        return SPELLS
                .stream()
                .filter(spell -> spell.getEntityType() == entityType)
                .findFirst()
                .orElse(null);
    }
}
