package seia.vanillamagic.handler;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import seia.vanillamagic.api.event.EventCustomTileEntity;
import seia.vanillamagic.api.handler.CustomTileEntityHandlerAPI;
import seia.vanillamagic.api.tileentity.ICustomTileEntity;
import seia.vanillamagic.util.BlockPosUtil;
import seia.vanillamagic.util.EntityUtil;
import seia.vanillamagic.util.EventUtil;
import seia.vanillamagic.util.WorldUtil;

/**
 * Try to use it via API with {@link CustomTileEntityHandlerAPI}.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class CustomTileEntityHandler {
	/**
	 * None {@link ICustomTileEntity} can be placed in empty space.<br>
	 * Any {@link ICustomTileEntity} placed there, should be deleted.
	 */
	public static final BlockPos EMPTY_SPACE = BlockPos.ORIGIN;

	private CustomTileEntityHandler() {
	}

	/**
	 * @return VM Root directory.
	 */
	public static String getRootDir() {
		return WorldHandler.getVanillaMagicRootDirectory().getAbsolutePath();
	}

	/**
	 * @see CustomTileEntityHandlerAPI#addCustomTileEntity(ICustomTileEntity, int)
	 */
	public static boolean addCustomTileEntity(ICustomTileEntity customTileEntity, int dimension) {
		WorldServer world = DimensionManager.getWorld(dimension);
		BlockPos customTilePos = customTileEntity.getTileEntity().getPos();
		ICustomTileEntity customTile = getCustomTileEntity(customTilePos, (World) world);

		if ((customTile == null)
				&& !EventUtil.postEvent(new EventCustomTileEntity.Add.Before(customTile, world, customTilePos))) {
			boolean added = world.addTileEntity(customTileEntity.getTileEntity());
			EventUtil.postEvent(new EventCustomTileEntity.Add.After(customTile, world, customTilePos));
			return added;
		}
		return false;
	}

	/**
	 * @see CustomTileEntityHandlerAPI#removeCustomTileEntityAtPos(World, BlockPos)
	 */
	public static boolean removeCustomTileEntityAtPos(World world, BlockPos pos) {
		for (int i = 0; i < world.tickableTileEntities.size(); ++i) {
			TileEntity tile = world.tickableTileEntities.get(i);

			if (BlockPosUtil.isSameBlockPos(tile.getPos(), pos)) {
				ICustomTileEntity customTile = getCustomTileEntity(tile.getPos(), world);

				if (!EventUtil.postEvent(new EventCustomTileEntity.Remove.Before(customTile, world, pos))) {
					world.tickableTileEntities.remove(i);
					EventUtil.postEvent(new EventCustomTileEntity.Remove.After(null, world, pos));
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @see CustomTileEntityHandlerAPI#removeCustomTileEntityAndSendInfoToPlayer(World,
	 *      BlockPos, EntityPlayer)
	 */
	public static void removeCustomTileEntityAndSendInfoToPlayer(World world, BlockPos pos, EntityPlayer player) {
		ICustomTileEntity customTile = CustomTileEntityHandler.getCustomTileEntity(pos,
				WorldUtil.getDimensionID(world));
		if (customTile == null) {
			return;
		}

		if (CustomTileEntityHandler.removeCustomTileEntityAtPos(world, pos) && !EventUtil
				.postEvent(new EventCustomTileEntity.Remove.Before.SendInfoToPlayer(customTile, world, pos, player))) {
			EntityUtil.addChatComponentMessageNoSpam(player, customTile.getClass().getSimpleName() + " removed");
			EventUtil
					.postEvent(new EventCustomTileEntity.Remove.After.SendInfoToPlayer(customTile, world, pos, player));
		}
	}

	public static List<ICustomTileEntity> getCustomEntitiesInDimension(int dimension) {
		WorldServer world = DimensionManager.getWorld(dimension);
		List<ICustomTileEntity> list = new ArrayList<ICustomTileEntity>();

		for (int i = 0; i < world.tickableTileEntities.size(); ++i) {
			TileEntity tile = world.tickableTileEntities.get(i);

			if (tile instanceof ICustomTileEntity) {
				list.add((ICustomTileEntity) tile);
			}
		}
		return list;
	}

	/**
	 * @see CustomTileEntityHandlerAPI#getCustomTileEntity(BlockPos, World)
	 */
	@Nullable
	public static ICustomTileEntity getCustomTileEntity(BlockPos tilePos, World world) {
		return getCustomTileEntity(tilePos, WorldUtil.getDimensionID(world));
	}

	/**
	 * @see CustomTileEntityHandlerAPI#getCustomTileEntity(BlockPos, int)
	 */
	@Nullable
	public static ICustomTileEntity getCustomTileEntity(BlockPos tilePos, int dimension) {
		WorldServer world = DimensionManager.getWorld(dimension);

		for (int i = 0; i < world.tickableTileEntities.size(); ++i) {
			TileEntity tile = world.tickableTileEntities.get(i);

			if ((BlockPosUtil.isSameBlockPos(tile.getPos(), tilePos)) && (tile instanceof ICustomTileEntity)) {
				EventUtil.postEvent(new EventCustomTileEntity((ICustomTileEntity) tile, world, tile.getPos()));
				return (ICustomTileEntity) tile;
			}
		}
		return null;
	}
}