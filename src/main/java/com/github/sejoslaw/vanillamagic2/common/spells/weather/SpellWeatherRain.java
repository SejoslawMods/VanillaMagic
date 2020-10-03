package com.github.sejoslaw.vanillamagic2.common.spells.weather;

import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellWeatherRain extends SpellWeather {
    public void cast(PlayerEntity player, IWorld world, BlockPos pos, Direction face) {
        world.getWorldInfo().setRaining(true);

        World worldObj = WorldUtils.asWorld(world);
        worldObj.rainingStrength = 1;
        worldObj.thunderingStrength = 0;
    }
}
