package com.github.sejoslaw.vanillamagic2.common.utils;

import net.minecraft.block.Block;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class AltarUtils {
    public static final Map<Integer, Block> BLOCKS = new HashMap<>();

    /**
     * Checks if the Altar is build correctly at specified position.
     *
     * @return True if the  Altar is build correctly; otherwise false.
     */
    public static boolean checkAltarTier(World world, BlockPos pos, int tier) {
        Block block = BLOCKS.get(tier);

        if (tier <= 1) {
            return checkTierNSidesOnly(world, pos.up(), tier, block) &&
                   checkTierNCornersOnly(world, pos.up(), tier, block);
        } else {
            if (checkAltarTier(world, pos, tier - 1)) {
                return tier % 2 == 0 ? checkTierNSidesOnly(world, pos, tier, block) : checkTierNCornersOnly(world, pos, tier, block);
            }
        }

        return false;
    }

    /**
     * @return True if the Altar contains the required ingredients. otherwise false.
     */
    public static boolean canCraftOnAltar(List<ItemStack> ingredients, List<ItemEntity> ingredientsInCauldron) {
        List<ItemEntity> validItemEntities = new ArrayList<>();

        for (ItemStack currentlyCheckedIngredient : ingredients) {
            for (ItemEntity currentlyCheckedItemEntity : ingredientsInCauldron) {
                if (ItemStack.areItemStacksEqual(currentlyCheckedIngredient, currentlyCheckedItemEntity.getItem())) {
                    validItemEntities.add(currentlyCheckedItemEntity);
                    break;
                }
            }
        }

        return ingredients.size() == validItemEntities.size();
    }

    private static boolean checkTierNCornersOnly(World world, BlockPos pos, int distance, Block block) {
        BlockPos up = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ() + distance);
        BlockPos left = new BlockPos(pos.getX() - distance, pos.getY() - 1, pos.getZ());
        BlockPos down = new BlockPos(pos.getX(), pos.getY() - 1, pos.getZ() - distance);
        BlockPos right = new BlockPos(pos.getX() + distance, pos.getY() - 1, pos.getZ());

        return world.getBlockState(up).getBlock() == block &&
               world.getBlockState(left).getBlock() == block &&
               world.getBlockState(down).getBlock() == block &&
               world.getBlockState(right).getBlock() == block;
    }

    private static boolean checkTierNSidesOnly(World world, BlockPos pos, int distance, Block block) {
        BlockPos rightUp = new BlockPos(pos.getX() + distance, pos.getY() - 1, pos.getZ() + distance);
        BlockPos leftUp = new BlockPos(pos.getX() - distance, pos.getY() - 1, pos.getZ() + distance);
        BlockPos leftDown = new BlockPos(pos.getX() - distance, pos.getY() - 1, pos.getZ() - distance);
        BlockPos rightDown = new BlockPos(pos.getX() + distance, pos.getY() - 1, pos.getZ() - distance);

        return world.getBlockState(rightUp).getBlock() == block &&
               world.getBlockState(leftUp).getBlock() == block &&
               world.getBlockState(leftDown).getBlock() == block &&
               world.getBlockState(rightDown).getBlock() == block;
    }
}
