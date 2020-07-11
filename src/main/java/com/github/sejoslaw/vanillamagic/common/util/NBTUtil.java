package com.github.sejoslaw.vanillamagic.common.util;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;

import javax.annotation.Nullable;

/**
 * Stores all the Vanilla Magic NBT tags. <br>
 * Additional tags are stored in interfaces.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class NBTUtil {
    // Machine position
    public static final String NBT_MACHINE_POS_X = "NBT_MACHINE_POS_X";
    public static final String NBT_MACHINE_POS_Y = "NBT_MACHINE_POS_Y";
    public static final String NBT_MACHINE_POS_Z = "NBT_MACHINE_POS_Z";

    // Working position - for instance: in Quarry
    public static final String NBT_WORKING_POS_X = "NBT_WORKING_POS_X";
    public static final String NBT_WORKING_POS_Y = "NBT_WORKING_POS_Y";
    public static final String NBT_WORKING_POS_Z = "NBT_WORKING_POS_Z";

    // Start working position
    public static final String NBT_START_POS_X = "NBT_START_POS_X";
    public static final String NBT_START_POS_Y = "NBT_START_POS_Y";
    public static final String NBT_START_POS_Z = "NBT_START_POS_Z";

    // Chest input position
    public static final String NBT_CHEST_POS_INPUT_X = "NBT_CHEST_POS_INPUT_X";
    public static final String NBT_CHEST_POS_INPUT_Y = "NBT_CHEST_POS_INPUT_Y";
    public static final String NBT_CHEST_POS_INPUT_Z = "NBT_CHEST_POS_INPUT_Z";

    // Chest output position
    public static final String NBT_CHEST_POS_OUTPUT_X = "NBT_CHEST_POS_OUTPUT_X";
    public static final String NBT_CHEST_POS_OUTPUT_Y = "NBT_CHEST_POS_OUTPUT_Y";
    public static final String NBT_CHEST_POS_OUTPUT_Z = "NBT_CHEST_POS_OUTPUT_Z";

    // Position tags
    public static final String NBT_POSX = "NBT_POSX";
    public static final String NBT_POSY = "NBT_POSY";
    public static final String NBT_POSZ = "NBT_POSZ";

    // Other tags
    public static final String NBT_RADIUS = "NBT_RADIUS";
    public static final String NBT_ONE_OPERATION_COST = "NBT_ONE_OPERATION_COST";
    public static final String NBT_TICKS = "NBT_TICKS";
    public static final String NBT_MAX_TICKS = "NBT_MAX_TICKS";
    public static final String NBT_IS_ACTIVE = "NBT_IS_ACTIVE";
    public static final String NBT_DIMENSION = "NBT_DIMENSION";
    public static final String NBT_NEEDS_FUEL = "NBT_NEEDS_FUEL";
    public static final String NBT_LOCALIZED_NAME_BLOCK = "NBT_LOCALIZED_NAME_BLOCK";
    public static final String NBT_HAS_TILEENTITY = "NBT_HAS_TILEENTITY";
    public static final String NBT_TAG_COMPOUND_NAME = "NBT_TAG_COMPOUND_NAME";
    public static final String NBT_IINVENTORY_ITEMS = "NBT_IINVENTORY_ITEMS";
    public static final String NBT_IINVENTORY_SLOT = "NBT_IINVENTORY_SLOT";
    public static final String NBT_SERIALIZABLE = "NBT_SERIALIZABLE";
    public static final String NBT_IITEMHANDLER_SLOT = "NBT_IITEMHANDLER_SLOT";
    public static final String NBT_IITEMHANDLER_ITEMS = "NBT_IITEMHANDLER_ITEMS";
    public static final String NBT_CLASS_NAME = "NBT_CLASS_NAME";
    public static final String NBT_TAG_COMPOUND_ENTITY = "NBTQuestCaptureEntity";
    public static final String NBT_ENTITY_TYPE = "EntityType";
    public static final String NBT_ENTITY_NAME = "EntityName";
    public static final String NBT_SPEEDY_TICKS = "NBT_SPEEDY_TICKS";
    public static final String NBT_TICKS_REMAINING = "NBT_TICKS_REMAINING";
    public static final String NBT_BLOCK_NAME = "NBT_BLOCK_NAME";
    public static final String NBT_BLOCK_STATE = "NBT_BLOCK_STATE";
    public static final String NBT_BLOCK = "NBT_BLOCK_ID";
    public static final String NBT_PLAYER_NAME = "NBT_PLAYER_NAME";
    public static final String NBT_CLASS = "NBT_CLASS";
    public static final String NBT_CRAFTING_SLOT = "NBT_CRAFTING_SLOT";

    private NBTUtil() {
    }

    @Nullable
    public static BlockPos getBlockPosDataFromNBT(CompoundNBT nbt) {
        if (!nbt.contains(NBT_POSX) || !nbt.contains(NBT_POSY) || !nbt.contains(NBT_POSZ)) {
            return null;
        }

        int posX = nbt.getInt(NBT_POSX);
        int posY = nbt.getInt(NBT_POSY);
        int posZ = nbt.getInt(NBT_POSZ);

        return new BlockPos(posX, posY, posZ);
    }

    public static int getDimensionFromNBT(CompoundNBT nbt) {
        if (nbt == null || !nbt.contains(NBT_DIMENSION)) {
            return DimensionType.OVERWORLD.getId();
        }

        return nbt.getInt(NBT_DIMENSION);
    }

    public static CompoundNBT setBlockPosDataToNBT(CompoundNBT nbt, BlockPos pos, DimensionType dimensionType) {
        if (nbt == null) {
            nbt = new CompoundNBT();
        }

        if (pos == null) {
            return nbt;
        }

        nbt.putInt(NBT_POSX, pos.getX());
        nbt.putInt(NBT_POSY, pos.getY());
        nbt.putInt(NBT_POSZ, pos.getZ());
        nbt.putInt(NBT_DIMENSION, dimensionType.getId());

        return nbt;
    }

    public static CompoundNBT getTagSafe(CompoundNBT tag, String key) {
        return (tag == null || !tag.contains(key)) ? new CompoundNBT() : tag.getCompound(key);
    }
}
