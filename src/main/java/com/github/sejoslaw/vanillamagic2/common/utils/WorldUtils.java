package com.github.sejoslaw.vanillamagic2.common.utils;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class WorldUtils {
    /**
     * @return All ItemEntities on specified position.
     */
    public static List<ItemEntity> getItems(World world, BlockPos pos) {
        AxisAlignedBB aabb = new AxisAlignedBB(
                pos.getX() - 0.5D,
                pos.getY() - 0.5D,
                pos.getZ() - 0.5D,
                pos.getX() + 0.5D,
                pos.getY() + 0.5D,
                pos.getZ() + 0.5D);
        return new ArrayList<>(world.getEntitiesWithinAABB(ItemEntity.class, aabb));
    }

    /**
     * @return All Ores on specified position.
     */
    public static List<ItemEntity> getOres(World world, BlockPos pos) {
        return getItems(world, pos)
                .stream()
                .filter(entity -> entity.getItem().getItem().getRegistryName().toString().toLowerCase().contains("ore"))
                .collect(Collectors.toList());
    }
}
