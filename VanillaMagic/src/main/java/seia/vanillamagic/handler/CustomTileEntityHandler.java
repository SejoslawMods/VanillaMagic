package seia.vanillamagic.handler;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import seia.vanillamagic.api.handler.CustomTileEntityHandlerAPI;
import seia.vanillamagic.api.tileentity.ICustomTileEntity;
import seia.vanillamagic.core.VanillaMagic;
import seia.vanillamagic.util.BlockPosHelper;
import seia.vanillamagic.util.WorldHelper;

/**
 * Try to use it via API with {@link CustomTileEntityHandlerAPI}
 */
public class CustomTileEntityHandler
{
	/**
	 * None {@link ICustomTileEntity} can be placed in empty space.<br>
	 * Any {@link ICustomTileEntity} placed there, should be deleted.
	 */
	public static final BlockPos EMPTY_SPACE = BlockPos.ORIGIN;
	
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
	public static boolean addCustomTileEntity(ICustomTileEntity customTileEntity, int dimension)
	{
		WorldServer world = DimensionManager.getWorld(dimension);
		ICustomTileEntity customTileOnWantedPos = getCustomTileEntity(customTileEntity.getTileEntity().getPos(), (World) world);
		// If NULL than there is no CustomTile on the position on which we want to place new CustomTile.
		// We can place our new CustomTile.
		if(customTileOnWantedPos == null)
		{
			return world.addTileEntity(customTileEntity.getTileEntity());
		}
		return false;
	}
	
	/**
	 * @see CustomTileEntityHandlerAPI#removeCustomTileEntityAtPos(World, BlockPos)
	 */
	public static boolean removeCustomTileEntityAtPos(World world, BlockPos pos)
	{
		for(int i = 0; i < world.tickableTileEntities.size(); ++i)
		{
			TileEntity tile = world.tickableTileEntities.get(i);
			if(BlockPosHelper.isSameBlockPos(tile.getPos(), pos))
			{
				world.tickableTileEntities.remove(i);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @see CustomTileEntityHandlerAPI#removeCustomTileEntityAtPos(World, BlockPos)
	 */
	@Deprecated
	public static boolean removeCustomTileEntityAtPos(World world, BlockPos pos, int dimension)
	{
		for(int i = 0; i < world.tickableTileEntities.size(); ++i)
		{
			TileEntity tile = world.tickableTileEntities.get(i);
			if(BlockPosHelper.isSameBlockPos(tile.getPos(), pos))
			{
				world.tickableTileEntities.remove(i);
				return true;
			}
		}
		return false;
	}
	
	public static List<ICustomTileEntity> getCustomEntitiesInDimension(int dimension)
	{
		WorldServer world = DimensionManager.getWorld(dimension);
		List<ICustomTileEntity> list = new ArrayList<ICustomTileEntity>();
		for(int i = 0; i < world.tickableTileEntities.size(); ++i)
		{
			TileEntity tile = world.tickableTileEntities.get(i);
			if(tile instanceof ICustomTileEntity)
			{
				list.add((ICustomTileEntity) tile);
			}
		}
		return list;
	}
	
	/**
	 * @see CustomTileEntityHandlerAPI#getCustomTileEntity(BlockPos, World)
	 */
	@Nullable
	public static ICustomTileEntity getCustomTileEntity(BlockPos tilePos, World world)
	{
		return getCustomTileEntity(tilePos, WorldHelper.getDimensionID(world));
	}
	
	/**
	 * @see CustomTileEntityHandlerAPI#getCustomTileEntity(BlockPos, int)
	 */
	@Nullable
	public static ICustomTileEntity getCustomTileEntity(BlockPos tilePos, int dimension)
	{
		WorldServer world = DimensionManager.getWorld(dimension);
		for(int i = 0; i < world.tickableTileEntities.size(); ++i)
		{
			TileEntity tile = world.tickableTileEntities.get(i);
			if(BlockPosHelper.isSameBlockPos(tile.getPos(), tilePos))
			{
				if(tile instanceof ICustomTileEntity)
				{
					return (ICustomTileEntity) tile;
				}
			}
		}
		return null;
	}
}