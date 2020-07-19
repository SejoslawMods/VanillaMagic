package com.github.sejoslaw.vanillamagic2.common.utils;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class NbtUtils {
    public static final String NBT_CUSTOM_ITEM_UNIQUE_NAME = "NBT_CUSTOM_ITEM_UNIQUE_NAME";
    public static final String NBT_SPAWNER_ENTITY = "NBT_SPAWNER_ENTITY";
    public static final String NBT_CAPTURED = "NBT_CAPTURED";
    public static final String NBT_ENTITY_TYPE = "NBT_ENTITY_TYPE";
    public static final String NBT_ENTITY_NAME = "NBT_ENTITY_NAME";
    public static final String NBT_POSITION = "NBT_POSITION";

    public static final String NBT_POS_X = "NBT_POS_X";
    public static final String NBT_POS_Y = "NBT_POS_Y";
    public static final String NBT_POS_Z = "NBT_POS_Z";
    public static final String NBT_DIMENSION = "NBT_DIMENSION";

    /**
     * @return NBT serialized position on specified World.
     */
    public static CompoundNBT toNbt(World world, BlockPos pos) {
        CompoundNBT nbt = new CompoundNBT();

        nbt.putInt(NBT_POS_X, pos.getX());
        nbt.putInt(NBT_POS_Y, pos.getY());
        nbt.putInt(NBT_POS_Z, pos.getZ());
        nbt.putInt(NBT_DIMENSION, world.getDimension().getType().getId());

        return  nbt;
    }

    /**
     * @return Position from given NBT.
     */
    public static BlockPos getPos(CompoundNBT nbt) {
        return new BlockPos(nbt.getInt(NBT_POS_X), nbt.getInt(NBT_POS_Y), nbt.getInt(NBT_POS_Z));
    }

    /**
     * @return World from saved data.
     */
    public static World getWorld(MinecraftServer server, CompoundNBT nbt) {
        int dimId = nbt.getInt(NBT_DIMENSION);
        DimensionType dimensionType = DimensionType.getById(dimId);
        return server.getWorld(dimensionType);
    }
}
