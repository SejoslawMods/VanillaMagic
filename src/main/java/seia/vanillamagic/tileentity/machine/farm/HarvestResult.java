package seia.vanillamagic.tileentity.machine.farm;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.BlockPos;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class HarvestResult implements IHarvestResult {
	public final List<EntityItem> drops;
	public final List<BlockPos> harvestedBlocks;

	public HarvestResult(List<EntityItem> drops, List<BlockPos> harvestedBlocks) {
		this.drops = drops;
		this.harvestedBlocks = harvestedBlocks;
	}

	public HarvestResult(List<EntityItem> drops, BlockPos harvestedBlock) {
		this.drops = drops;
		this.harvestedBlocks = new ArrayList<BlockPos>();
		this.harvestedBlocks.add(harvestedBlock);
	}

	public HarvestResult() {
		this.drops = new ArrayList<EntityItem>();
		this.harvestedBlocks = new ArrayList<BlockPos>();
	}

	public List<EntityItem> getDrops() {
		return this.drops;
	}

	public List<BlockPos> getHarvestedBlocks() {
		return this.harvestedBlocks;
	}
}