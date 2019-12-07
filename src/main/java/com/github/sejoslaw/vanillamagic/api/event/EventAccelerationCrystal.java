package com.github.sejoslaw.vanillamagic.api.event;

import com.github.sejoslaw.vanillamagic.api.item.ICustomItem;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Base class for all Acceleration Crystal related events.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventAccelerationCrystal extends EventCustomItem.OnUseByPlayer {
    public EventAccelerationCrystal(ICustomItem customItem, World world, BlockPos clickedPos, PlayerEntity player) {
        super(customItem, player, world, clickedPos);
    }

    /**
     * This Event is fired when Acceleration Crystal speeds up normal block.
     */
    public static class TickBlock extends EventAccelerationCrystal {
        public TickBlock(ICustomItem customItem, World world, BlockPos clickedPos, PlayerEntity player) {
            super(customItem, world, clickedPos, player);
        }
    }

    /**
     * This Event is fired when Acceleration Crystal speeds up TileEntity.
     */
    public static class TickTileEntity extends EventAccelerationCrystal {
        private final TileEntity clickedTile;

        public TickTileEntity(ICustomItem customItem, World world, BlockPos clickedPos, PlayerEntity player, TileEntity clickedTile) {
            super(customItem, world, clickedPos, player);
            this.clickedTile = clickedTile;
        }

        /**
         * @return Returns the TileEntity which was clicked with Acceleration Crystal.
         * This should always be ITickable.
         */
        public TileEntity getTileEntity() {
            return clickedTile;
        }
    }
}