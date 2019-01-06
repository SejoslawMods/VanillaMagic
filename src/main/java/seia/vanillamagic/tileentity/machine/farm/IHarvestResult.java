package seia.vanillamagic.tileentity.machine.farm;

import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.BlockPos;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface IHarvestResult {
	List<EntityItem> getDrops();

	List<BlockPos> getHarvestedBlocks();
}