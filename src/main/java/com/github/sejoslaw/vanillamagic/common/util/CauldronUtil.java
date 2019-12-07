package com.github.sejoslaw.vanillamagic.common.util;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Class which store various methods connected with Cauldron.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class CauldronUtil {
    private CauldronUtil() {
    }

    public static List<ItemEntity> getItemsInCauldron(World world, BlockPos cauldronPos) {
		AxisAlignedBB aabb = new AxisAlignedBB(
				cauldronPos.getX(),
				cauldronPos.getY(),
				cauldronPos.getZ(),
				cauldronPos.getX() + 0.5D,
				cauldronPos.getY() + 0.5D,
				cauldronPos.getZ() + 0.5D);

		return world.getEntitiesWithinAABB(EntityType.ITEM, aabb, entity -> true)
				.stream()
				.map(entity -> (ItemEntity)entity)
				.collect(Collectors.toList());
    }
}