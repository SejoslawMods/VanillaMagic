package seia.vanillamagic.tileentity.machine.farm;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.BlockPos;

public interface IHarvestResult 
{
	List<EntityItem> getDrops();
	
	List<BlockPos> getHarvestedBlocks();
}