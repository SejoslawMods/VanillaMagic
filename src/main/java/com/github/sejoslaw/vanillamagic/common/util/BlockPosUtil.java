package com.github.sejoslaw.vanillamagic.common.util;

import com.github.sejoslaw.vanillamagic.core.VMLogger;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which store various methods connected with Block Position.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class BlockPosUtil {
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
}
