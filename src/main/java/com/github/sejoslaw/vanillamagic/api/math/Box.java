package com.github.sejoslaw.vanillamagic.api.math;

import net.minecraft.util.math.BlockPos;

/**
 * Simple 3D Box implementation.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class Box {
    public int xMin;
    public int yMin;
    public int zMin;
    public int xMax;
    public int yMax;
    public int zMax;

    public Box(BlockPos pos) {
        this(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
    }

    public Box(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax) {
        this.xMin = xMin;
        this.yMin = yMin;
        this.zMin = zMin;
        this.xMax = xMax;
        this.yMax = yMax;
        this.zMax = zMax;
    }

    public void resizeX(int size) {
        xMin = xMin - size;
        xMax = xMax + size;
    }

    public void resizeY(int size) {
        yMin = yMin - size;
        yMax = yMax + size;
    }

    public void resizeZ(int size) {
        zMin = zMin - size;
        zMax = zMax + size;
    }
}