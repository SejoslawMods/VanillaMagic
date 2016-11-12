package seia.vanillamagic.handler.customtileentity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.api.handler.CustomTileEntityHandlerAPI;
import seia.vanillamagic.api.tileentity.ICustomTileEntity;
import seia.vanillamagic.core.VanillaMagic;
import seia.vanillamagic.handler.WorldHandler;

public class CustomTileEntityHandler 
{
	/**
	 * None {@link ICustomTileEntity} can be placed in empty space.<br>
	 * Any {@link ICustomTileEntity} placed there, should be deleted.
	 */
	public static final BlockPos EMPTY_SPACE = new BlockPos(0, 0, 0);
	
	private static final Map<String, CustomTileEntityOneSaveHandler> SAVE_HANDLERS = new HashMap<String, CustomTileEntityOneSaveHandler>();
	
	private CustomTileEntityHandler()
	{
	}
	
	public static void postInit()
	{
		VanillaMagic.LOGGER.log(Level.INFO, "CustomTileEntityHandler registered");
	}
	
	public static String getRootDir()
	{
		return WorldHandler.getVanillaMagicRootDirectory().getAbsolutePath();
	}
	
	/**
	 * @see CustomTileEntityHandlerAPI#addCustomTileEntity(ICustomTileEntity, int)
	 */
	public static boolean addCustomTileEntity(ICustomTileEntity customTileEntity, int dimensionID)
	{
		String rootDir = getRootDir();
		if(!SAVE_HANDLERS.containsKey(rootDir))
		{
			SAVE_HANDLERS.put(rootDir, new CustomTileEntityOneSaveHandler());
		}
		return SAVE_HANDLERS.get(rootDir).addCustomTileEntity(customTileEntity, dimensionID);
	}
	
	/**
	 * @see CustomTileEntityHandlerAPI#removeCustomTileEntityAtPos(World, BlockPos)
	 */
	public static boolean removeCustomTileEntityAtPos(World world, BlockPos pos)
	{
		String rootDir = getRootDir();
		if(SAVE_HANDLERS.containsKey(rootDir))
		{
			return SAVE_HANDLERS.get(rootDir).removeCustomTileEntityAtPos(world, pos);
		}
		return false;
	}
	
	/**
	 * @see CustomTileEntityHandler#removeCustomTileEntityAtPos(World, BlockPos)
	 */
	@Deprecated
	public static boolean removeCustomTileEntityAtPos(World world, BlockPos pos, int dimension)
	{
		String rootDir = getRootDir();
		if(SAVE_HANDLERS.containsKey(rootDir))
		{
			return SAVE_HANDLERS.get(rootDir).removeCustomTileEntityAtPos(world, pos, dimension);
		}
		return false;
	}
	
	public static List<ICustomTileEntity> getCustomEntitiesInDimension(int dimension)
	{
		String rootDir = getRootDir();
		if(!SAVE_HANDLERS.containsKey(rootDir))
		{
			SAVE_HANDLERS.put(rootDir, new CustomTileEntityOneSaveHandler());
		}
		return SAVE_HANDLERS.get(rootDir).getCustomEntitiesInDimension(dimension);
	}
	
	/**
	 * @see CustomTileEntityHandler#getCustomTileEntity(BlockPos, int)
	 */
	@Nullable
	public static ICustomTileEntity getCustomTileEntity(BlockPos tilePos, int dimension)
	{
		String rootDir = getRootDir();
		if(!SAVE_HANDLERS.containsKey(rootDir))
		{
			SAVE_HANDLERS.put(rootDir, new CustomTileEntityOneSaveHandler());
		}
		return SAVE_HANDLERS.get(rootDir).getCustomTileEntity(tilePos, dimension);
	}
}