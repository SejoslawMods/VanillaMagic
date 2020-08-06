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
    public static final String NBT_ENTITY_TYPE = "NBT_ENTITY_TYPE";
    public static final String NBT_ENTITY_NAME = "NBT_ENTITY_NAME";

    public static final String NBT_BLOCK = "NBT_BLOCK";
    public static final String NBT_BLOCK_STATE = "NBT_BLOCK_STATE";

    public static final String NBT_POSITION = "NBT_POSITION";
    public static final String NBT_DIMENSION = "NBT_DIMENSION";

    public static final String NBT_VM_ITEM_UNIQUE_NAME = "NBT_VM_ITEM_UNIQUE_NAME";
    public static final String NBT_SPAWNER_ENTITY = "NBT_SPAWNER_ENTITY";
    public static final String NBT_CAPTURED = "NBT_CAPTURED";
    public static final String NBT_SPELL_ID = "NBT_SPELL_ID";
    public static final String NBT_TICKS = "NBT_TICKS";
    public static final String NBT_MACHINE_MODULE_KEY = "NBT_MACHINE_MODULE_KEY";

    public static final String NBT_MODULE_START_POS = "NBT_MODULE_START_POS";
    public static final String NBT_MODULE_WORKING_POS = "NBT_MODULE_WORKING_POS";
    public static final String NBT_MODULE_STORAGE_INPUT_POS = "NBT_MODULE_STORAGE_INPUT_POS";
    public static final String NBT_MODULE_STORAGE_OUTPUT_POS = "NBT_MODULE_STORAGE_OUTPUT_POS";
    public static final String NBT_MODULE_HAS_ENERGY = "NBT_MODULE_HAS_ENERGY";
    public static final String NBT_MODULE_ENERGY_SOURCE_POS = "NBT_MODULE_ENERGY_SOURCE_POS";

    /**
     * @return NBT serialized position on specified World.
     */
    public static CompoundNBT toNbt(World world, BlockPos pos) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putLong(NBT_POSITION, pos.toLong());
        nbt.putInt(NBT_DIMENSION, world.getDimension().getType().getId());
        return  nbt;
    }

    /**
     * @return Position from given NBT.
     */
    public static BlockPos getPos(CompoundNBT nbt) {
        return BlockPos.fromLong(nbt.getLong(NBT_POSITION));
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
