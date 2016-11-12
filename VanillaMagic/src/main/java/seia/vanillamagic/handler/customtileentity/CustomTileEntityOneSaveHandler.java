package seia.vanillamagic.handler.customtileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.api.tileentity.ICustomTileEntity;
import seia.vanillamagic.core.VanillaMagic;
import seia.vanillamagic.util.BlockPosHelper;
import seia.vanillamagic.util.WorldHelper;

public class CustomTileEntityOneSaveHandler
{
	protected Map<Integer, List<ICustomTileEntity>> loadedTileEntities;
	
	public CustomTileEntityOneSaveHandler()
	{
		loadedTileEntities = new HashMap<Integer, List<ICustomTileEntity>>();
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
					BlockPosHelper.printCoords(Level.WARN, "There is already CustomTileEntity (" + 
												tile.getClass().getSimpleName() + ") at pos:", tile.getTileEntity().getPos());
					return false;
				}
			}
			entitiesInDim.add(customTileEntity);
			add(customTileEntity);
			return true;
		}
		else
		{
			loadedTileEntities.put(dimension, new ArrayList<ICustomTileEntity>());
			VanillaMagic.LOGGER.log(Level.INFO, "Registered CustomTileEntityHandler for Dimension: " + dimension);
			loadedTileEntities.get(dimension).add(customTileEntity);
			add(customTileEntity);
			return true;
		}
	}
	
	private void add(ICustomTileEntity customTileEntity)
	{
		TileEntity tile = customTileEntity.getTileEntity();
		if(BlockPosHelper.isSameBlockPos(tile.getPos(), CustomTileEntityHandler.EMPTY_SPACE))
		{
			return;
		}
		World world = tile.getWorld();
		world.addTileEntity(tile);
		BlockPosHelper.printCoords(Level.INFO, "CustomTileEntity (" + 
									customTileEntity.getClass().getSimpleName() + ") added at pos:", tile.getPos());
		world.updateEntities();
	}
	
	public boolean removeCustomTileEntityAtPos(World world, BlockPos pos)
	{
		int dimID = WorldHelper.getDimensionID(world);
		return removeCustomTileEntityAtPos(world, pos, dimID);
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
				for(int j = 0; j < world.tickableTileEntities.size(); j++)
				{
					if(BlockPosHelper.isSameBlockPos(world.tickableTileEntities.get(j).getPos(), tileInDim.getTileEntity().getPos()))
					{
						world.tickableTileEntities.remove(j);
						tilesInDimension.remove(i);
						BlockPosHelper.printCoords(Level.INFO, "Removed CustomTileEntity (" + 
													tileInDim.getClass().getSimpleName() + ") at:", pos);
						return true;
					}
				}
			}
		}
		// Tile is not on list - added after loading
		for(int i = 0; i < tilesInDimension.size(); i++)
		{
			ICustomTileEntity tileInDim = tilesInDimension.get(i);
			if(BlockPosHelper.isSameBlockPos(tileInDim.getTileEntity().getPos(), pos))
			{
				tilesInDimension.remove(i);
				BlockPosHelper.printCoords(Level.INFO, "Removed CustomTileEntity (" + 
											tileInDim.getClass().getSimpleName() + ") at:", pos);
				return true;
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
			loadedTileEntities.put(dimension, new ArrayList<ICustomTileEntity>());
		}
		return loadedTileEntities.get(dimension);
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