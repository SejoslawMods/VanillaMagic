package com.github.sejoslaw.vanillamagic2.common.utils;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class EntityUtils {
    /**
     * @return Name of the Player.
     */
    public static String getPlayerName(PlayerEntity player) {
        return player.getName().getFormattedText();
    }

    /**
     * @return Name of the World.
     */
    public static String getWorldName(PlayerEntity player) {
        return player.getEntityWorld().getServer().getWorldName();
    }

    /**
     * @return List with registered types equal with the one that is specified.
     */
    public static List<EntityType<?>> getEntitiesByClassification(EntityClassification classification) {
        return ForgeRegistries.ENTITIES
                .getValues()
                .stream()
                .filter(type -> type.getClassification() == classification)
                .collect(Collectors.toList());
    }
}
