package seia.vanillamagic.handler.customtileentity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.handler.WorldHandler;

public class CustomTileEntityHandler 
{
	public static final CustomTileEntityHandler INSTANCE = new CustomTileEntityHandler();
	
	protected Map<String, CustomTileEntityOneSaveHandler> saveHandlers = new HashMap<String, CustomTileEntityOneSaveHandler>();
	
	private CustomTileEntityHandler()
	{
	}
	
	public void postInit()
	{
		System.out.println("CustomTileEntityHandler registered");
	}
	
	public boolean addCustomTileEntity(TileEntity customTileEntity, int dimensionID)
	{
		String rootDir = WorldHandler.getVanillaMagicRootDirectory().getAbsolutePath();
		if(saveHandlers.containsKey(rootDir))
		{
			return saveHandlers.get(rootDir).addCustomTileEntity(customTileEntity, dimensionID);
		}
		else
		{
			saveHandlers.put(rootDir, new CustomTileEntityOneSaveHandler());
			return saveHandlers.get(rootDir).addCustomTileEntity(customTileEntity, dimensionID);
		}
	}
	
	public void removeCustomTileEntityAtPos(World world, BlockPos pos, int dimension)
	{
		String rootDir = WorldHandler.getVanillaMagicRootDirectory().getAbsolutePath();
		if(saveHandlers.containsKey(rootDir))
		{
			saveHandlers.get(rootDir).removeCustomTileEntityAtPos(world, pos, dimension);
		}
	}
	
	public List<TileEntity> getCustomEntitiesInDimension(int dimension)
	{
		String rootDir = WorldHandler.getVanillaMagicRootDirectory().getAbsolutePath();
		if(saveHandlers.containsKey(rootDir))
		{
			return saveHandlers.get(rootDir).getCustomEntitiesInDimension(dimension);
		}
		return new ArrayList<TileEntity>();
	}
}