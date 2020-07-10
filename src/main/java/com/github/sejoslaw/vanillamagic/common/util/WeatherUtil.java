package com.github.sejoslaw.vanillamagic.common.util;

import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Class which holds methods connected with MC Weather interaction.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class WeatherUtil {
    private WeatherUtil() {
    }

    /**
     * Spawn Lightning Bolt.
     *
     * @param world World on which Lightning Bolt should be spawned.
     * @param pos   Position
     */
    public static void spawnLightningBolt(World world, BlockPos pos) {
        LightningBoltEntity bolt = new LightningBoltEntity(world, pos.getX(), pos.getY(), pos.getZ(), false);
        world.addEntity(bolt);
    }
}