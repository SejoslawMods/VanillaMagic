package seia.vanillamagic.util;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Class which store various methods connected with Cauldron.
 */
public class CauldronHelper 
{
	private CauldronHelper()
	{
	}
	
	public static List<EntityItem> getItemsInCauldron(World world, BlockPos cauldronPos)
	{
		// all entities in World
		List<Entity> loadedEntities = world.loadedEntityList;
		// all items in cauldron
		List<EntityItem> entitiesInCauldron = new ArrayList<EntityItem>();
		if(loadedEntities.size() == 0)
		{
			return entitiesInCauldron;
		}
		// filtering all items in cauldron to check if the recipe is correct
		for(int i = 0; i < loadedEntities.size(); ++i)
		{
			if(loadedEntities.get(i) instanceof EntityItem)
			{
				EntityItem entityItemInWorld = (EntityItem) loadedEntities.get(i);
				BlockPos entityItemInWorldPos = new BlockPos(entityItemInWorld.posX, entityItemInWorld.posY, entityItemInWorld.posZ);
				if(BlockPosHelper.isSameBlockPos(cauldronPos, entityItemInWorldPos))
				{
					entitiesInCauldron.add(entityItemInWorld);
				}
			}
		}
		return entitiesInCauldron;
	}
}