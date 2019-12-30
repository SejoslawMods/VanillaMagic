package com.github.sejoslaw.vanillamagic.common.tileentity.machine.farm;

import com.github.sejoslaw.vanillamagic.api.tileentity.machine.farm.IHarvestResult;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class HarvestResult implements IHarvestResult {
    public final List<ItemEntity> drops;
    public final List<BlockPos> harvestedBlocks;

    public HarvestResult(List<ItemEntity> drops, List<BlockPos> harvestedBlocks) {
        this.drops = drops;
        this.harvestedBlocks = harvestedBlocks;
    }

    public HarvestResult(List<ItemEntity> drops, BlockPos harvestedBlock) {
        this.drops = drops;
        this.harvestedBlocks = new ArrayList<>();
        this.harvestedBlocks.add(harvestedBlock);
    }

    public HarvestResult() {
        this.drops = new ArrayList<>();
        this.harvestedBlocks = new ArrayList<>();
    }

    public List<ItemEntity> getDrops() {
        return this.drops;
    }

    public List<BlockPos> getHarvestedBlocks() {
        return this.harvestedBlocks;
    }
}