package seia.vanillamagic.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Class which store various methods connected with Cauldron.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class CauldronUtil {
	private CauldronUtil() {
	}

	public static List<EntityItem> getItemsInCauldron(World world, BlockPos cauldronPos) {
		List<Entity> loadedEntities = world.loadedEntityList;
		List<EntityItem> entitiesInCauldron = new ArrayList<EntityItem>();

		if (loadedEntities.size() == 0) {
			return entitiesInCauldron;
		}

		for (int i = 0; i < loadedEntities.size(); ++i) {
			if (loadedEntities.get(i) instanceof EntityItem) {
				EntityItem entityItemInWorld = (EntityItem) loadedEntities.get(i);
				BlockPos entityItemInWorldPos = new BlockPos(entityItemInWorld.posX, entityItemInWorld.posY,
						entityItemInWorld.posZ);

				if (BlockPosUtil.isSameBlockPos(cauldronPos, entityItemInWorldPos)) {
					entitiesInCauldron.add(entityItemInWorld);
				}
			}
		}

		return entitiesInCauldron;
	}
}