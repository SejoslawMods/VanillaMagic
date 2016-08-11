package seia.vanillamagic.handler;

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
import net.minecraftforge.common.DimensionManager;
import seia.vanillamagic.utils.BlockPosHelper;
import seia.vanillamagic.utils.IDimensionKeeper;

public class CustomTileEntityHandler 
{
	public static final CustomTileEntityHandler INSTANCE = new CustomTileEntityHandler();
	
	public static final int ERROR_DIMENSION_ID = -100;
	
	static
	{
		Integer[] dimIDs = DimensionManager.getIDs();
		for(Integer i : dimIDs)
		{
			INSTANCE.tileEntities.put(i, new ArrayList<TileEntity>());
		}
	}
	
	//===============================================================================================
	
	private final Map<Integer, List<TileEntity>> tileEntities = new HashMap<Integer, List<TileEntity>>();
	
	public boolean loaded = false;
	public boolean saved = false;
	
	private CustomTileEntityHandler()
	{
	}
	
	public void postInit()
	{
		System.out.println("CustomTileEntityHandler registered");
	}
	
	/**
	 * customTileEntity MUST implements {@link IDimensionKeeper} <br>
	 * customTileEntity MUST implements {@link ITickable}
	 */
	public boolean addCustomTileEntity(TileEntity customTileEntity)
	{
		BlockPosHelper.printCoords("Trying to add TileEntity (" + customTileEntity.getClass().getSimpleName() + ")...", customTileEntity.getPos());  // TODO:
		int dimension = ((IDimensionKeeper) customTileEntity).getDimension();
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
		TileEntity tile = world.getTileEntity(pos);
		if(tile instanceof IDimensionKeeper)
		{
			int dimID = ((IDimensionKeeper) tile).getDimension();
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
		else
		{
			BlockPosHelper.printCoords("Destroyed TileEntity is not a IDimensionKeeper", pos);
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
	
	public int getDimensionFromTickables(World world)
	{
		List<TileEntity> tickableTileEntities = world.tickableTileEntities;
		for(TileEntity tile : tickableTileEntities)
		{
			if(tile instanceof IDimensionKeeper)
			{
				return ((IDimensionKeeper) tile).getDimension();
			}
		}
		return ERROR_DIMENSION_ID;
	}
}