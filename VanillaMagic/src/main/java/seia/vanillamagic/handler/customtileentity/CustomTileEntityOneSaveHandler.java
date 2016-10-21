package seia.vanillamagic.handler.customtileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.VanillaMagic;
import seia.vanillamagic.api.tileentity.ICustomTileEntity;
import seia.vanillamagic.util.BlockPosHelper;
import seia.vanillamagic.util.WorldHelper;

public class CustomTileEntityOneSaveHandler
{
	protected final Map<Integer, List<ICustomTileEntity>> loadedTileEntities;
	public final Map<Integer, List<ICustomTileEntity>> readedTileEntities;
	
	public CustomTileEntityOneSaveHandler()
	{
		loadedTileEntities = new HashMap<Integer, List<ICustomTileEntity>>();
		readedTileEntities = new HashMap<Integer, List<ICustomTileEntity>>();
	}
	
	public void clearTileEntities()
	{
		loadedTileEntities.clear();
	}
	
	public void clearTileEntitiesForDimension(int dimension)
	{
		loadedTileEntities.get(dimension).clear();
	}
	
	/**
	 * customTileEntity MUST implements {@link ITickable}
	 */
	public boolean addCustomTileEntity(ICustomTileEntity customTileEntity, int dimension)
	{
		if(loadedTileEntities.containsKey(dimension))
		{
			List<ICustomTileEntity> entitiesInDim = loadedTileEntities.get(dimension);
			for(ICustomTileEntity tile : entitiesInDim)
			{
				if(BlockPosHelper.isSameBlockPos(customTileEntity.getTileEntity().getPos(), tile.getTileEntity().getPos()))
				{
					BlockPosHelper.printCoords(Level.WARN, "There is already CustomTileEntity (" + tile.getClass().getSimpleName() + ") at pos:", tile.getTileEntity().getPos());
					return false;
				}
			}
			entitiesInDim.add(customTileEntity);
			add(customTileEntity);
			return true;
		}
		else
		{
			loadedTileEntities.put(new Integer(dimension), new ArrayList<ICustomTileEntity>());
			VanillaMagic.LOGGER.log(Level.INFO, "Registered CustomTileEntityHandler for Dimension: " + dimension);
			loadedTileEntities.get(dimension).add(customTileEntity);
			add(customTileEntity);
			return true;
		}
	}
	
	private void add(ICustomTileEntity customTileEntity)
	{
		customTileEntity.getTileEntity().getWorld().setTileEntity(customTileEntity.getTileEntity().getPos(), customTileEntity.getTileEntity());
		//customTileEntity.getWorld().tickableTileEntities.add(customTileEntity); // TODO: CustomTileEntity should be added to List<ITickable> in World.
		BlockPosHelper.printCoords(Level.INFO, "CustomTileEntity (" + customTileEntity.getClass().getSimpleName() + ") added at pos:", customTileEntity.getTileEntity().getPos());
		customTileEntity.getTileEntity().getWorld().updateEntities();
	}
	
	/**
	 * TileEntity at position "pos" MUST implements {@link IDimensionKeeper}
	 */
	public boolean removeCustomTileEntityAtPos(World world, BlockPos pos)
	{
		int dimID = WorldHelper.getDimensionID(world);
//		Set<Entry<Integer, List<ICustomTileEntity>>> entrySet = loadedTileEntities.entrySet();
//		if(entrySet.size() > 0) // should be 3 (Dims: -1, 0, 1, and more)
//		{
//			for(Entry<Integer, List<ICustomTileEntity>> entry : entrySet)
//			{
//				if(entry.getKey().intValue() == dimID)
//				{
					return removeCustomTileEntityAtPos(world, pos, dimID);
//				}
//			}
//			BlockPosHelper.printCoords(Level.WARN, "Didn't found the TileEntity at pos:", pos);
//		}
//		return false;
	}
	
	public boolean removeCustomTileEntityAtPos(World world, BlockPos pos, int dimension)
	{
		List<ICustomTileEntity> tilesInDimension = loadedTileEntities.get(dimension);
		if(tilesInDimension == null)
		{
			return false;
		}
		if(tilesInDimension.size() == 0)
		{
			return false;
		}
		for(int i = 0; i < tilesInDimension.size(); i++)
		{
			ICustomTileEntity tileInDim = tilesInDimension.get(i);
			if(BlockPosHelper.isSameBlockPos(tileInDim.getTileEntity().getPos(), pos))
			{
				// tilesInDimension.remove(i);
				for(int j = 0; j < world.tickableTileEntities.size(); j++)
				{
					if(BlockPosHelper.isSameBlockPos(world.tickableTileEntities.get(j).getPos(), tileInDim.getTileEntity().getPos()))
					{
						world.tickableTileEntities.remove(j);
						tilesInDimension.remove(i);
						BlockPosHelper.printCoords(Level.INFO, "Removed CustomTileEntity (" + tileInDim.getClass().getSimpleName() + ") at:", pos);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public Integer[] getIDs()
	{
		return loadedTileEntities.keySet().toArray(new Integer[loadedTileEntities.size()]);
	}
	
	public List<ICustomTileEntity> getCustomEntitiesInDimension(int dimension)
	{
		List<ICustomTileEntity> tiles = loadedTileEntities.get(dimension);
		if(tiles == null)
		{
			loadedTileEntities.put(new Integer(dimension), new ArrayList<ICustomTileEntity>());
		}
		return loadedTileEntities.get(dimension);
	}

	public void moveTilesFromReadded(int dimension) 
	{
		loadedTileEntities.get(dimension).addAll(readedTileEntities.get(dimension));
		readedTileEntities.get(dimension).clear();
	}

	public List<ICustomTileEntity> getReadedTileEntitiesForDimension(int dimension) 
	{
		List<ICustomTileEntity> tiles = readedTileEntities.get(dimension);
		if(tiles == null)
		{
			readedTileEntities.put(new Integer(dimension), new ArrayList<ICustomTileEntity>());
		}
		return readedTileEntities.get(dimension);
	}

	public boolean addReadedTile(ICustomTileEntity tileEntity, int dimension)
	{
		if(readedTileEntities.containsKey(dimension))
		{
			List<ICustomTileEntity> entitiesInDim = readedTileEntities.get(dimension);
			for(ICustomTileEntity tile : entitiesInDim)
			{
				if(BlockPosHelper.isSameBlockPos(tileEntity.getTileEntity().getPos(), tile.getTileEntity().getPos()))
				{
					BlockPosHelper.printCoords(Level.WARN, "There is already ReadedTileEntity (" + tile.getClass().getSimpleName() + ") at pos:", tile.getTileEntity().getPos());
					return false;
				}
			}
			entitiesInDim.add(tileEntity);
			return true;
		}
		else
		{
			readedTileEntities.put(new Integer(dimension), new ArrayList<ICustomTileEntity>());
			VanillaMagic.LOGGER.log(Level.INFO, "Registered ReadedTileEntityHandler for Dimension: " + dimension);
			readedTileEntities.get(dimension).add(tileEntity);
			return true;
		}
	}

	@Nullable
	public ICustomTileEntity getCustomTileEntity(BlockPos tilePos, int dimension) 
	{
		List<ICustomTileEntity> tiles = loadedTileEntities.get(dimension);
		if(tiles == null)
		{
			return null;
		}
		for(ICustomTileEntity tile : tiles)
		{
			if(BlockPosHelper.isSameBlockPos(tilePos, tile.getTileEntity().getPos()))
			{
				return tile;
			}
		}
		return null;
	}
}