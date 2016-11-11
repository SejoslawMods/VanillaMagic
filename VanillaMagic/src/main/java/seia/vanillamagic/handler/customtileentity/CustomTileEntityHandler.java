package seia.vanillamagic.handler.customtileentity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.api.tileentity.ICustomTileEntity;
import seia.vanillamagic.core.VanillaMagic;
import seia.vanillamagic.handler.WorldHandler;

public class CustomTileEntityHandler 
{
	public static final CustomTileEntityHandler INSTANCE = new CustomTileEntityHandler();
	
	protected final Map<String, CustomTileEntityOneSaveHandler> saveHandlers;
	
	private CustomTileEntityHandler()
	{
		saveHandlers = new HashMap<String, CustomTileEntityOneSaveHandler>();
	}
	
	public void postInit()
	{
		VanillaMagic.LOGGER.log(Level.INFO, "CustomTileEntityHandler registered");
	}
	
	public String getRootDir()
	{
		return WorldHandler.getVanillaMagicRootDirectory().getAbsolutePath();
	}
	
	public boolean addCustomTileEntity(ICustomTileEntity customTileEntity, int dimensionID)
	{
		String rootDir = getRootDir();
		if(!saveHandlers.containsKey(rootDir))
		{
			saveHandlers.put(rootDir, new CustomTileEntityOneSaveHandler());
		}
		return saveHandlers.get(rootDir).addCustomTileEntity(customTileEntity, dimensionID);
	}
	
	public boolean removeCustomTileEntityAtPos(World world, BlockPos pos)
	{
		String rootDir = getRootDir();
		if(saveHandlers.containsKey(rootDir))
		{
			return saveHandlers.get(rootDir).removeCustomTileEntityAtPos(world, pos);
		}
		return false;
	}
	
	/**
	 * @see CustomTileEntityHandler#removeCustomTileEntityAtPos(World, BlockPos)
	 */
	@Deprecated
	public boolean removeCustomTileEntityAtPos(World world, BlockPos pos, int dimension)
	{
		String rootDir = getRootDir();
		if(saveHandlers.containsKey(rootDir))
		{
			return saveHandlers.get(rootDir).removeCustomTileEntityAtPos(world, pos, dimension);
		}
		return false;
	}
	
	public List<ICustomTileEntity> getCustomEntitiesInDimension(int dimension)
	{
		String rootDir = getRootDir();
		if(!saveHandlers.containsKey(rootDir))
		{
			saveHandlers.put(rootDir, new CustomTileEntityOneSaveHandler());
		}
		return saveHandlers.get(rootDir).getCustomEntitiesInDimension(dimension);
	}
	
	public List<ICustomTileEntity> getReaddedTileEntitiesForDimension(int dimension)
	{
		String rootDir = getRootDir();
		if(!saveHandlers.containsKey(rootDir))
		{
			saveHandlers.put(rootDir, new CustomTileEntityOneSaveHandler());
		}
		return saveHandlers.get(rootDir).getReadedTileEntitiesForDimension(dimension);
	}

	public void moveTilesFromReadded(int dimension) 
	{
		String rootDir = getRootDir();
		if(saveHandlers.containsKey(rootDir))
		{
			saveHandlers.get(rootDir).moveTilesFromReadded(dimension);
		}
	}

	public boolean addReadedTile(ICustomTileEntity tileEntity, int dimension)
	{
		String rootDir = getRootDir();
		if(!saveHandlers.containsKey(rootDir))
		{
			saveHandlers.put(rootDir, new CustomTileEntityOneSaveHandler());
		}
		return saveHandlers.get(rootDir).addReadedTile(tileEntity, dimension);
	}
	
	@Nullable
	public ICustomTileEntity getCustomTileEntity(BlockPos tilePos, int dimension)
	{
		String rootDir = getRootDir();
		if(!saveHandlers.containsKey(rootDir))
		{
			saveHandlers.put(rootDir, new CustomTileEntityOneSaveHandler());
		}
		return saveHandlers.get(rootDir).getCustomTileEntity(tilePos, dimension);
	}
}