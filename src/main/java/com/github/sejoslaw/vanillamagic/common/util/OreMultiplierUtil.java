package com.github.sejoslaw.vanillamagic.common.util;

import net.minecraft.block.FurnaceBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Class which store various methods connected with Ore Multiplier. Checking
 * structure, etc.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class OreMultiplierUtil {
    private OreMultiplierUtil() {
    }

    public static boolean check(World world, BlockPos cauldronPos) {
        BlockPos furnaceNorth = cauldronPos.offset(Direction.NORTH);
        BlockPos furnaceSouth = cauldronPos.offset(Direction.SOUTH);
        BlockPos furnaceEast = cauldronPos.offset(Direction.EAST);
        BlockPos furnaceWest = cauldronPos.offset(Direction.WEST);

        return world.getBlockState(furnaceNorth).getBlock() instanceof FurnaceBlock
                && world.getBlockState(furnaceSouth).getBlock() instanceof FurnaceBlock
                && world.getBlockState(furnaceEast).getBlock() instanceof FurnaceBlock
                && world.getBlockState(furnaceWest).getBlock() instanceof FurnaceBlock;
    }
}