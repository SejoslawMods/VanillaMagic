package com.github.sejoslaw.vanillamagic.api.tileentity.machine.farm;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.math.BlockPos;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface IHarvestResult {
    List<ItemEntity> getDrops();

    List<BlockPos> getHarvestedBlocks();
}