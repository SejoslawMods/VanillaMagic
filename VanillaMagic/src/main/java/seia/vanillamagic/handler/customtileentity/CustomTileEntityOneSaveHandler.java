package seia.vanillamagic.handler.customtileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.utils.BlockPosHelper;
import seia.vanillamagic.utils.WorldHelper;

public class CustomTileEntityOneSaveHandler
{
	protected final Map<Integer, List<TileEntity>> tileEntities;
	
	public CustomTileEntityOneSaveHandler()
	{
		tileEntities = new HashMap<Integer, List<TileEntity>>();
	}
	
	public void clearTileEntities()
	{
		tileEntities.clear();
	}
	
	public void clearTileEntitiesForDimension(int dimension)
	{
		tileEntities.get(dimension).clear();
	}
	
	/**
	 * customTileEntity MUST implements {@link ITickable}
	 */
	public boolean addCustomTileEntity(TileEntity customTileEntity, int dimension)
	{
		if(tileEntities.containsKey(dimension))
		{
			List<TileEntity> entitiesInDim = tileEntities.get(dimension);
			for(TileEntity tile : entitiesInDim)
			{
				if(BlockPosHelper.isSameBlockPos(customTileEntity.getPos(), tile.getPos()))
				{
					BlockPosHelper.printCoords("There is already CustomTileEntity (" + tile.getClass().getSimpleName() + ") at pos:", tile.getPos());
					return false;
				}
			}
			entitiesInDim.add(customTileEntity);
			add(customTileEntity);
			return true;
		}
		else
		{
			tileEntities.put(new Integer(dimension), new ArrayList<TileEntity>());
			System.out.println("Registered CustomTileEntityHandler for Dimension: " + dimension);
			tileEntities.get(dimension).add(customTileEntity);
			add(customTileEntity);
			return true;
		}
	}
	
	private void add(TileEntity customTileEntity)
	{
		customTileEntity.getWorld().setTileEntity(customTileEntity.getPos(), customTileEntity);
		customTileEntity.getWorld().tickableTileEntities.add(customTileEntity);
		BlockPosHelper.printCoords("CustomTileEntity (" + customTileEntity.getClass().getSimpleName() + ") added at pos:", customTileEntity.getPos());
	}
	
	/**
	 * TileEntity at position "pos" MUST implements {@link IDimensionKeeper}
	 */
	public void removeCustomTileEntityAtPos(World world, BlockPos pos)
	{
		int dimID = WorldHelper.getDimensionID(world);
		Set<Entry<Integer, List<TileEntity>>> entrySet = tileEntities.entrySet();
		if(entrySet.size() > 0) // should be 3 (Dims: -1, 0, 1, and more)
		{
			for(Entry<Integer, List<TileEntity>> entry : entrySet)
			{
				if(entry.getKey().intValue() == dimID)
				{
					removeCustomTileEntityAtPos(world, pos, dimID);
				}
			}
			BlockPosHelper.printCoords("Didn't found the TileEntity at pos:", pos);
		}
	}
	
	public void removeCustomTileEntityAtPos(World world, BlockPos pos, int dimension)
	{
		List<TileEntity> tilesInDimension = tileEntities.get(dimension);
		for(int i = 0; i < tilesInDimension.size(); i++)
		{
			TileEntity tileInDim = tilesInDimension.get(i);
			if(BlockPosHelper.isSameBlockPos(tileInDim.getPos(), pos))
			{
				BlockPosHelper.printCoords("Removed CustomTileEntity (" + tileInDim.getClass().getSimpleName() + ") at:", pos);
				tilesInDimension.remove(i);
				for(int j = 0; j < world.tickableTileEntities.size(); j++)
				{
					if(BlockPosHelper.isSameBlockPos(world.tickableTileEntities.get(j).getPos(), tileInDim.getPos()))
					{
						world.tickableTileEntities.remove(j);
					}
				}
				return;
			}
		}
	}
	
	public Integer[] getIDs()
	{
		return tileEntities.keySet().toArray(new Integer[tileEntities.size()]);
	}
	
	public List<TileEntity> getCustomEntitiesInDimension(int dimension)
	{
		return tileEntities.get(dimension);
	}
}