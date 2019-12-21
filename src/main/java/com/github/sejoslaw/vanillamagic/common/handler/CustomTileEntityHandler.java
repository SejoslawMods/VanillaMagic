package com.github.sejoslaw.vanillamagic.common.handler;

import com.github.sejoslaw.vanillamagic.api.event.EventCustomTileEntity;
import com.github.sejoslaw.vanillamagic.api.tileentity.ICustomTileEntity;
import com.github.sejoslaw.vanillamagic.common.util.EntityUtil;
import com.github.sejoslaw.vanillamagic.common.util.EventUtil;
import com.github.sejoslaw.vanillamagic.common.util.TextUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * Try to use it via API with CustomTileEntityHandlerAPI.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class CustomTileEntityHandler {
    /**
     * None ICustomTileEntity can be placed in empty space.<br>
     * Any ICustomTileEntity placed there, should be deleted.
     */
    public static final BlockPos EMPTY_SPACE = BlockPos.ZERO;

    private CustomTileEntityHandler() {
    }

    public static boolean addCustomTileEntity(ICustomTileEntity customTileEntity, World world) {
        BlockPos customTilePos = customTileEntity.getTileEntity().getPos();
        ICustomTileEntity customTile = getCustomTileEntity(world, customTilePos);

        if ((customTile == null) && !EventUtil.postEvent(new EventCustomTileEntity.Add.Before(customTile, world, customTilePos))) {
            boolean added = world.addTileEntity(customTileEntity.getTileEntity());
            EventUtil.postEvent(new EventCustomTileEntity.Add.After(customTile, world, customTilePos));
            return added;
        }

        return false;
    }

    public static boolean removeCustomTileEntityAtPos(World world, BlockPos pos) {
        for (int i = 0; i < world.tickableTileEntities.size(); ++i) {
            TileEntity tile = world.tickableTileEntities.get(i);

            if (tile.getPos().equals(pos)) {
                ICustomTileEntity customTile = getCustomTileEntity(world, tile.getPos());

                if (!EventUtil.postEvent(new EventCustomTileEntity.Remove.Before(customTile, world, pos))) {
                    world.tickableTileEntities.remove(i);
                    EventUtil.postEvent(new EventCustomTileEntity.Remove.After(null, world, pos));
                    return true;
                }
            }
        }

        return false;
    }

    public static void removeCustomTileEntityAndSendInfoToPlayer(World world, BlockPos pos, PlayerEntity player) {
        ICustomTileEntity customTile = CustomTileEntityHandler.getCustomTileEntity(world, pos);

        if (customTile == null) {
            return;
        }

        if (CustomTileEntityHandler.removeCustomTileEntityAtPos(world, pos) && !EventUtil.postEvent(new EventCustomTileEntity.Remove.Before.SendInfoToPlayer(customTile, world, pos, player))) {
            EntityUtil.addChatComponentMessageNoSpam(player, TextUtil.wrap(customTile.getClass().getSimpleName() + " removed"));
            EventUtil.postEvent(new EventCustomTileEntity.Remove.After.SendInfoToPlayer(customTile, world, pos, player));
        }
    }

    public static List<ICustomTileEntity> getCustomEntitiesInDimension(World world) {
        List<ICustomTileEntity> list = new ArrayList<>();

        for (int i = 0; i < world.tickableTileEntities.size(); ++i) {
            TileEntity tile = world.tickableTileEntities.get(i);

            if (tile instanceof ICustomTileEntity) {
                list.add((ICustomTileEntity) tile);
            }
        }

        return list;
    }

    public static ICustomTileEntity getCustomTileEntity(World world, BlockPos tilePos) {
        for (int i = 0; i < world.tickableTileEntities.size(); ++i) {
            TileEntity tile = world.tickableTileEntities.get(i);

            if (tile.getPos().equals(tilePos) && (tile instanceof ICustomTileEntity)) {
                EventUtil.postEvent(new EventCustomTileEntity((ICustomTileEntity) tile, world, tile.getPos()));

                return (ICustomTileEntity) tile;
            }
        }

        return null;
    }
}