package com.github.sejoslaw.vanillamagic.common.util;

import com.github.sejoslaw.vanillamagic.core.VMLogger;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which store various methods connected with Block Position.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class BlockPosUtil {
    public static final int SEARCH_RADIUS = 100;

    private BlockPosUtil() {
    }

    public static int distanceInLine(BlockPos pos1, BlockPos pos2) {
        if ((pos1.getX() == pos2.getX()) && (pos1.getY() == pos2.getY())) {
            return Math.max(pos1.getZ(), pos2.getZ()) - Math.min(pos1.getZ(), pos2.getZ());
        } else if ((pos1.getZ() == pos2.getZ()) && (pos1.getY() == pos2.getY())) {
            return Math.max(pos1.getX(), pos2.getX()) - Math.min(pos1.getX(), pos2.getX());
        } else {
            return Math.max(pos1.getY(), pos2.getY()) - Math.min(pos1.getY(), pos2.getY());
        }
    }

    public static void printCoords(BlockPos pos) {
        VMLogger.logInfo(" X = " + pos.getX());
        VMLogger.logInfo(" Y = " + pos.getY());
        VMLogger.logInfo(" Z = " + pos.getZ());
    }

    public static void printCoords(Level level, String text, BlockPos pos) {
        VMLogger.log(level, text);
        printCoords(pos);
    }

    public static void printCoords(Level level, Block block, BlockPos pos) {
        VMLogger.log(level, block.toString());
        printCoords(pos);
    }

    public static void printCoords(Level level, World world, BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();
        printCoords(level, block, pos);

        VMLogger.logInfo(" Dimension = " + world.getDimension().getType().getId());
    }

    public static void printCoords(Level level, PlayerEntity player, BlockPos pos) {
        printCoords(level, player.world, pos);

        VMLogger.logInfo(" Dimension = " + player.world.getDimension().getType().getId());
    }

    public static void freezeNearby(Entity entity, World world, BlockPos pos, int size) {
        BlockPos.MutableBlockPos blockPosCenter = new BlockPos.MutableBlockPos(0, 0, 0);

        BlockPos.getAllInBoxMutable(pos.add(-size, -1.0D, -size), pos.add(size, -1.0D, size))
                .forEach(blockPosAround -> {
                    if (blockPosAround.distanceSq(entity.posX, entity.posY, entity.posZ, true) <= (double) (size * size)) {
                        return;
                    }

                    blockPosCenter.setPos(blockPosAround.getX(), blockPosAround.getY() + 1, blockPosAround.getZ());
                    BlockState blockCenterState = world.getBlockState(blockPosCenter);

                    if (blockCenterState.getMaterial() != Material.AIR) {
                        return;
                    }

                    BlockState blockAroundState = world.getBlockState(blockPosAround);

                    if (blockAroundState.getMaterial() == Material.WATER) {
                        world.setBlockState(blockPosAround, Blocks.ICE.getDefaultState());
                    }
                });
    }

    public static List<BlockPos> getBlockPos3x3XZ(BlockPos pos) {
        List<BlockPos> blocks = new ArrayList<>();

        int centerX = pos.getX();
        int centerY = pos.getY();
        int centerZ = pos.getZ();

        blocks.add(pos);
        blocks.add(new BlockPos(centerX - 1, centerY, centerZ + 1)); // top-left
        blocks.add(new BlockPos(centerX, centerY, centerZ + 1)); // top
        blocks.add(new BlockPos(centerX + 1, centerY, centerZ + 1)); // top-right
        blocks.add(new BlockPos(centerX + 1, centerY, centerZ)); // right
        blocks.add(new BlockPos(centerX + 1, centerY, centerZ - 1)); // bottom-right
        blocks.add(new BlockPos(centerX, centerY, centerZ - 1)); // bottom
        blocks.add(new BlockPos(centerX - 1, centerY, centerZ - 1)); // bottom-left
        blocks.add(new BlockPos(centerX - 1, centerY, centerZ)); // left

        return blocks;
    }

    public static List<BlockPos> getBlockPos3x3x3(BlockPos centerPos) {
        List<BlockPos> blocks = new ArrayList<>();

        blocks.addAll(getBlockPos3x3XZ(centerPos.offset(Direction.UP)));
        blocks.addAll(getBlockPos3x3XZ(centerPos));
        blocks.addAll(getBlockPos3x3XZ(centerPos.offset(Direction.DOWN)));

        return blocks;
    }

    public static List<BlockPos> drawCircle2DXZ(int centerX, int posY, int centerZ, int radius, Block fillWith) {
        List<BlockPos> toFill = new ArrayList<>();

        int d = (5 - radius * 4) / 4;
        int x = 0;
        int z = radius;

        do {
            toFill.add(new BlockPos(centerX + x, posY, centerZ + z));
            toFill.add(new BlockPos(centerX + x, posY, centerZ + z));
            toFill.add(new BlockPos(centerX + x, posY, centerZ - z));
            toFill.add(new BlockPos(centerX - x, posY, centerZ + z));
            toFill.add(new BlockPos(centerX - x, posY, centerZ - z));
            toFill.add(new BlockPos(centerX + z, posY, centerZ + x));
            toFill.add(new BlockPos(centerX + z, posY, centerZ - x));
            toFill.add(new BlockPos(centerX - z, posY, centerZ + x));
            toFill.add(new BlockPos(centerX - z, posY, centerZ - x));

            if (d < 0) {
                d += 2 * x + 1;
            } else {
                d += 2 * (x - z) + 1;
                z--;
            }

            x++;
        } while (x <= z);

        return toFill;
    }
}