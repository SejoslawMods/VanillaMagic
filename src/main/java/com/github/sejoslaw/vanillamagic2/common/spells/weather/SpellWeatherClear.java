package com.github.sejoslaw.vanillamagic2.common.spells.weather;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldInfo;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellWeatherClear extends SpellWeather {
    public void cast(PlayerEntity player, World world, BlockPos pos, Direction face) {
        WorldInfo worldInfo = world.getWorldInfo();
        worldInfo.setClearWeatherTime(1000);
        worldInfo.setRainTime(0);
        worldInfo.setThunderTime(0);
        worldInfo.setRaining(false);
        worldInfo.setThundering(false);
    }
}
