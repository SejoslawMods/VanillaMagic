package seia.vanillamagic.api.handler;

import java.lang.reflect.Method;

import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import seia.vanillamagic.api.VanillaMagicAPI;
import seia.vanillamagic.api.tileentity.ICustomTileEntity;

/**
 * CustomTileEntityHandler is used to hold all {@link ICustomTileEntity}s.
 * Also it is use to Save / Load {@link ICustomTileEntity}s. <br>
 * <br>
 * To make Your CustomTile saving / loading properly You must register Your tile using full class name:<br>
 * GameRegistry.registerTileEntity(MyCustomTile.class, "my.package.with.tile.MyCustomTile");<br>
 * <br>
 * Adding / removing should be register after some action happened. 
 * See description of methods for more information.
 * 
 * @see GameRegistry#registerTileEntity(Class, String)
 */
public class CustomTileEntityHandlerAPI 
{
	/**
	 * Handler class
	 */
	private static final String HANDLER_CLASS = "seia.vanillamagic.handler.CustomTileEntityHandler";
	
	private CustomTileEntityHandlerAPI()
	{
	}
	
	/**
	 * This method should be use to register {@link ICustomTileEntity} into the CustomTileEntityHandler.<br>
	 * For instance VanillaMagic CustomTiles are registered after clicked on block (mainly).
	 * 
	 * @param customTileEntity ICustomTileEntity which should be added
	 * @param world World to which this tile should be added.
	 * @return TRUE if the tile was added successfully
	 */
	public static boolean addCustomTileEntity(ICustomTileEntity customTileEntity, World world)
	{
		return addCustomTileEntity(customTileEntity, world.provider.getDimension());
	}
	
	/**
	 * This method should be use to register {@link ICustomTileEntity} into the CustomTileEntityHandler.<br>
	 * For instance VanillaMagic CustomTiles are registered after clicked on block (mainly).
	 * 
	 * @param customTileEntity ICustomTileEntity which should be added
	 * @param dimensionID dimension to which this tile should be added. It should be - customTileEntity.getTileEntity().getWorld().provider.getDimension(); (Overworld is 0)
	 * @return TRUE if the tile was added successfully
	 */
	public static boolean addCustomTileEntity(ICustomTileEntity customTileEntity, int dimension)
	{
		try
		{
			Class<?> clazz = Class.forName(HANDLER_CLASS);
			Method method = clazz.getMethod("addCustomTileEntity", ICustomTileEntity.class, int.class);
			return (boolean) method.invoke(null, customTileEntity, dimension);
		}
		catch(Exception e)
		{
			VanillaMagicAPI.LOGGER.log(Level.ERROR, 
					"Error while adding ICustomTileEntity to World. (Class: " + customTileEntity.getClass().getName() + "), Dimension: " + 
					dimension);
			return false;
		}
	}
	
	/**
	 * This method should be use to delete the {@link ICustomTileEntity} at the specified position.<br>
	 * For instance VanillaMagic CustomTiles are removed if the block was destroyed (mainly).
	 * 
	 * @param world world from which we will delete the tile
	 * @param pos position of the deleting tile
	 * @return TRUE is the tile was removed successfully
	 */
	public static boolean removeCustomTileEntityAtPos(World world, BlockPos pos)
	{
		try
		{
			Class<?> clazz = Class.forName(HANDLER_CLASS);
			Method method = clazz.getMethod("removeCustomTileEntityAtPos", World.class, BlockPos.class);
			return (boolean) method.invoke(null, world, pos);
		}
		catch(Exception e)
		{
			VanillaMagicAPI.LOGGER.log(Level.ERROR, "Error while removing CustomTileEntity at position" +
										pos.toString());
			return false;
		}
	}
	
	/**
	 * This method should be use to delete the {@link ICustomTileEntity} at the specified position and inform {@link EntityPlayer} about it.<br>
	 * For instance VanillaMagic CustomTiles are removed if the block was destroyed (mainly).
	 * 
	 * @param world world from which we will delete the tile
	 * @param pos position of the deleting tile
	 * @param player player that should be informed about CustomTileEntity being removed
	 */
	public static void removeCustomTileEntityAndSendInfoToPlayer(World world, BlockPos pos, EntityPlayer player)
	{
		try
		{
			Class<?> clazz = Class.forName(HANDLER_CLASS);
			Method method = clazz.getMethod("removeCustomTileEntityAndSendInfoToPlayer", World.class, BlockPos.class, EntityPlayer.class);
			method.invoke(null, world, pos);
		}
		catch(Exception e)
		{
			VanillaMagicAPI.LOGGER.log(Level.ERROR, "Error while removing CustomTileEntity at position" +
										pos.toString());
		}
	}
	
	/**
	 * Returns the {@link ICustomTileEntity} at the specified position from the specified dimension.
	 * 
	 * @param tilePos position of the tile
	 * @param dimension dimension in which the tile is (Overworld is 0)
	 * @return the {@link ICustomTileEntity} at the specified position. NULL if there is no tile at the given position.
	 */
	@Nullable
	public static ICustomTileEntity getCustomTileEntity(BlockPos tilePos, int dimension)
	{
		try
		{
			Class<?> clazz = Class.forName(HANDLER_CLASS);
			Method method = clazz.getMethod("getCustomTileEntity", BlockPos.class, int.class);
			return (ICustomTileEntity) method.invoke(null, tilePos, dimension);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	/**
	 * Returns the {@link ICustomTileEntity} at the specified position from the specified dimension.
	 * 
	 * @param tilePos position of the tile
	 * @param world world on which the tile is
	 * @return the {@link ICustomTileEntity} at the specified position. NULL if there is no tile at the given position.
	 */
	@Nullable
	public static ICustomTileEntity getCustomTileEntity(BlockPos tilePos, World world)
	{
		try
		{
			Class<?> clazz = Class.forName(HANDLER_CLASS);
			Method method = clazz.getMethod("getCustomTileEntity", BlockPos.class, World.class);
			return (ICustomTileEntity) method.invoke(null, tilePos, world);
		}
		catch(Exception e)
		{
			return null;
		}
	}
}