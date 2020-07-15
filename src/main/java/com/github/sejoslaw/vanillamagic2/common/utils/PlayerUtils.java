package com.github.sejoslaw.vanillamagic2.common.utils;

import net.minecraft.entity.player.PlayerEntity;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class PlayerUtils {
    public static String getName(PlayerEntity player) {
        return player.getName().getFormattedText();
    }

    public static String getWorldName(PlayerEntity player) {
        return player.getEntityWorld().getServer().getWorldName();
    }
}
