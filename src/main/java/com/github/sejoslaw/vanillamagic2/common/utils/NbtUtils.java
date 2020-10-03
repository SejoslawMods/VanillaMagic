package com.github.sejoslaw.vanillamagic2.common.utils;

import com.github.sejoslaw.vanillamagic2.common.functions.Consumer3;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.server.ServerWorld;

import java.util.stream.StreamSupport;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class NbtUtils {
    public static final String NBT_ENTITY_TYPE = "NBT_ENTITY_TYPE";
    public static final String NBT_ENTITY_NAME = "NBT_ENTITY_NAME";

    public static final String NBT_BLOCK = "NBT_BLOCK";
    public static final String NBT_BLOCK_STATE = "NBT_BLOCK_STATE";
    public static final String NBT_TILE_TYPE = "NBT_TILE_TYPE";

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

    public static final CompoundNBT EMPTY_NBT = new CompoundNBT();

    /**
     * @return NBT serialized position on specified IWorld.
     */
    public static CompoundNBT toNbt(IWorld world, BlockPos pos) {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putLong(NBT_POSITION, pos.toLong());
        nbt.putString(NBT_DIMENSION, WorldUtils.getId(world).toString());
        return  nbt;
    }

    /**
     * @return Position from given NBT.
     */
    public static BlockPos getPos(CompoundNBT nbt) {
        return BlockPos.fromLong(nbt.getLong(NBT_POSITION));
    }

    /**
     * @return IWorld from saved data.
     */
    public static ServerWorld getWorld(MinecraftServer server, CompoundNBT nbt) {
        String dimId = nbt.getString(NBT_DIMENSION);
        return StreamSupport
                .stream(server.getWorlds().spliterator(), false)
                .filter(world -> world.getDimensionKey().getRegistryName().equals(dimId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Iterates over every entry in given NBT.
     *
     * @param consumer Indicates depth, key and value of a currently checking NBT node.
     */
    public static void forEachEntry(INBT nbt, Consumer3<Integer, String, String> consumer) {
        forEachEntry("NBT", nbt, 0, consumer);
    }

    private static void forEachEntry(String parentKey, INBT nbt, int depth, Consumer3<Integer, String, String> consumer) {
        if (nbt instanceof CompoundNBT) {
            handleNbtEntry(parentKey, EMPTY_NBT, depth, consumer);
            for (String key : ((CompoundNBT) nbt).keySet()) {
                INBT valueTag = ((CompoundNBT) nbt).get(key);
                forEachEntry(key, valueTag, depth + 1, consumer);
            }
        } else if (nbt instanceof ListNBT) {
            handleNbtEntry(parentKey, EMPTY_NBT, depth, consumer);
            for (int i = 0; i < ((ListNBT) nbt).size(); ++i) {
                INBT valueTag = ((ListNBT) nbt).get(i);
                forEachEntry(String.valueOf(i), valueTag, depth + 1, consumer);
            }
        } else {
            handleNbtEntry(parentKey, nbt, depth, consumer);
        }
    }

    private static void handleNbtEntry(String key, INBT nbt, int depth, Consumer3<Integer, String, String> consumer) {
        ITextComponent str = nbt.toFormattedComponent();
        consumer.accept(depth, key, str.getString());
    }
}
