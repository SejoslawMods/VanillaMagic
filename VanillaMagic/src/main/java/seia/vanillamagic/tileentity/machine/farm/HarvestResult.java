package seia.vanillamagic.tileentity.machine.farm;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.BlockPos;

public class HarvestResult implements IHarvestResult
{
	public final List<EntityItem> drops;
	public final List<BlockPos> harvestedBlocks;

	public HarvestResult(List<EntityItem> drops, List<BlockPos> harvestedBlocks) 
	{
		this.drops = drops;
		this.harvestedBlocks = harvestedBlocks;
	}

	public HarvestResult(List<EntityItem> drops, BlockPos harvestedBlock) 
	{
		this.drops = drops;
		this.harvestedBlocks = new ArrayList<BlockPos>();    
		harvestedBlocks.add(harvestedBlock);
	}

	public HarvestResult() 
	{
		drops = new ArrayList<EntityItem>();
		harvestedBlocks = new ArrayList<BlockPos>();
	}

	public List<EntityItem> getDrops() 
	{
		return drops;
	}

	public List<BlockPos> getHarvestedBlocks()
	{
		return harvestedBlocks;
	}
}