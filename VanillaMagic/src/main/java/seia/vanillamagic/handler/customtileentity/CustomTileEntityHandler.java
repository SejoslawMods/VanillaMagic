package seia.vanillamagic.handler.customtileentity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
		System.out.println("CustomTileEntityHandler registered");
	}
	
	public String getRootDir()
	{
		return WorldHandler.getVanillaMagicRootDirectory().getAbsolutePath();
	}
	
	public boolean addCustomTileEntity(TileEntity customTileEntity, int dimensionID)
	{
		String rootDir = getRootDir();
		if(!saveHandlers.containsKey(rootDir))
		{
			saveHandlers.put(rootDir, new CustomTileEntityOneSaveHandler());
		}
		return saveHandlers.get(rootDir).addCustomTileEntity(customTileEntity, dimensionID);
	}
	
	public void removeCustomTileEntityAtPos(World world, BlockPos pos, int dimension)
	{
		String rootDir = getRootDir();
		if(saveHandlers.containsKey(rootDir))
		{
			saveHandlers.get(rootDir).removeCustomTileEntityAtPos(world, pos, dimension);
		}
	}
	
	public List<TileEntity> getCustomEntitiesInDimension(int dimension)
	{
		String rootDir = getRootDir();
		if(!saveHandlers.containsKey(rootDir))
		{
			saveHandlers.put(rootDir, new CustomTileEntityOneSaveHandler());
		}
		return saveHandlers.get(rootDir).getCustomEntitiesInDimension(dimension);
	}
	
	public List<TileEntity> getReaddedTileEntitiesForDimension(int dimension)
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

	public boolean addReadedTile(TileEntity tileEntity, int dimension) 
	{
		String rootDir = getRootDir();
		if(!saveHandlers.containsKey(rootDir))
		{
			saveHandlers.put(rootDir, new CustomTileEntityOneSaveHandler());
		}
		return saveHandlers.get(rootDir).addReadedTile(tileEntity, dimension);
	}
	
	@Nullable
	public TileEntity getCustomTileEntity(BlockPos tilePos, int dimension)
	{
		String rootDir = getRootDir();
		if(!saveHandlers.containsKey(rootDir))
		{
			saveHandlers.put(rootDir, new CustomTileEntityOneSaveHandler());
		}
		return saveHandlers.get(rootDir).getCustomTileEntity(tilePos, dimension);
	}
}