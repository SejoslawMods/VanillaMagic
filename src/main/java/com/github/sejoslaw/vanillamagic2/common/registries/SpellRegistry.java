package com.github.sejoslaw.vanillamagic2.common.registries;

import com.github.sejoslaw.vanillamagic2.common.spells.Spell;
import com.github.sejoslaw.vanillamagic2.common.spells.normal.*;
import com.github.sejoslaw.vanillamagic2.common.spells.summon.SpellSummonEntity;
import com.github.sejoslaw.vanillamagic2.common.spells.weather.SpellWeatherClear;
import com.github.sejoslaw.vanillamagic2.common.spells.weather.SpellWeatherRain;
import com.github.sejoslaw.vanillamagic2.common.spells.weather.SpellWeatherThunderstorm;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class SpellRegistry {
    public static final Map<String, Spell> SPELLS = new HashMap<>();

    public static void initialize() {
        SPELLS.put("spellFlintAndSteel", new SpellLighter());
        SPELLS.put("spellSmallFireball", new SpellSmallFireball());
        SPELLS.put("spellLargeFireball", new SpellLargeFireball());
        SPELLS.put("spellTeleport", new SpellTeleport());
        SPELLS.put("spellSummonMeteor", new SpellSummonMeteor());
        SPELLS.put("spellSummonLightningBolt", new SpellSummonLightningBolt());
        SPELLS.put("spellFusRoDah", new SpellFusRoDah());
        SPELLS.put("spellTeleportToNether", new SpellTeleportToNether());
        SPELLS.put("spellTeleportToEnd", new SpellTeleportToEnd());
        SPELLS.put("spellMoveInAir", new SpellMoveInAir());
        SPELLS.put("spellPullEntityToPlayer", new SpellPullEntityToPlayer());
        SPELLS.put("spellFreezeWater3x3", new SpellFreezeWater());

        SPELLS.put("spellWeatherRain", new SpellWeatherRain());
        SPELLS.put("spellWeatherClear", new SpellWeatherClear());
        SPELLS.put("spellWeatherThunderstorm", new SpellWeatherThunderstorm());

        for (Map.Entry<ResourceLocation, EntityType<?>> entry : ForgeRegistries.ENTITIES.getEntries()) {
            String spellUniqueName = "spellSummon_" + entry.getKey().getPath();
            SPELLS.put(spellUniqueName, new SpellSummonEntity(entry.getValue()));
        }
    }
}
