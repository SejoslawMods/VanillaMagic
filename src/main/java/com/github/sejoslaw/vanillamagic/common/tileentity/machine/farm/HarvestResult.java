package com.github.sejoslaw.vanillamagic.tileentity.machine.farm;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.math.BlockPos;

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
		this.harvestedBlocks = new ArrayList<BlockPos>();
		this.harvestedBlocks.add(harvestedBlock);
	}

	public HarvestResult() {
		this.drops = new ArrayList<ItemEntity>();
		this.harvestedBlocks = new ArrayList<BlockPos>();
	}

	public List<ItemEntity> getDrops() {
		return this.drops;
	}

	public List<BlockPos> getHarvestedBlocks() {
		return this.harvestedBlocks;
	}
}