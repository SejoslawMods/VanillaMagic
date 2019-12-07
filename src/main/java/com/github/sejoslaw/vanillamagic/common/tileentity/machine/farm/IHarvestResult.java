package com.github.sejoslaw.vanillamagic.tileentity.machine.farm;

import java.util.List;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.math.BlockPos;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface IHarvestResult {
	List<ItemEntity> getDrops();

	List<BlockPos> getHarvestedBlocks();
}