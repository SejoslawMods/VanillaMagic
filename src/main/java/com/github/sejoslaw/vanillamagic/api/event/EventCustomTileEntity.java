package com.github.sejoslaw.vanillamagic.api.event;

import com.github.sejoslaw.vanillamagic.api.tileentity.ICustomTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Event which is fired when something happens connected with
 * {@link ICustomTileEntity}.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCustomTileEntity extends VMEvent {
    private final ICustomTileEntity customTileEntity;
    private final World world;
    private final BlockPos customTilePos;

    public EventCustomTileEntity(ICustomTileEntity customTileEntity, World world, BlockPos customTilePos) {
        this.customTileEntity = customTileEntity;
        this.world = world;
        this.customTilePos = customTilePos;
    }

    /**
     * @return Returns ICustomTileEntity connected with this Event. NULL after this
     * Tile has been deleted from World.
     */
    @Nullable
    public ICustomTileEntity getCustomTileEntity() {
        return customTileEntity;
    }

    /**
     * @return Returns the World on which ICustomTileEntity is.
     */
    public World getWorld() {
        return world;
    }

    /**
     * @return Returns the position of ICustomTileEntity.
     */
    public BlockPos getTilePos() {
        return customTilePos;
    }

    /**
     * This Event is fired when something connected with adding ICustomTileEntity to
     * the World happens.
     */
    public static class Add extends EventCustomTileEntity {
        public Add(ICustomTileEntity customTileEntity, World world, BlockPos customTilePos) {
            super(customTileEntity, world, customTilePos);
        }

        /**
         * This Event is fired BEFORE the ICustomTileEntity is added to World at given
         * position.
         */
        public static class Before extends Add {
            public Before(ICustomTileEntity customTileEntity, World world, BlockPos customTilePos) {
                super(customTileEntity, world, customTilePos);
            }
        }

        /**
         * This Event is fired AFTER the ICustomTileEntity has been added to World at
         * given position.
         */
        public static class After extends Add {
            public After(ICustomTileEntity customTileEntity, World world, BlockPos customTilePos) {
                super(customTileEntity, world, customTilePos);
            }
        }
    }

    /**
     * This Event is fired when something connected with removing TileEntity from
     * the World happens.
     */
    public static class Remove extends EventCustomTileEntity {
        public Remove(ICustomTileEntity customTileEntity, World world, BlockPos pos) {
            super(customTileEntity, world, pos);
        }

        /**
         * This Event is fired BEFORE the TileEntity is removed from World at given
         * position.
         */
        public static class Before extends Remove {
            public Before(ICustomTileEntity customTileEntity, World world, BlockPos pos) {
                super(customTileEntity, world, pos);
            }

            /**
             * This Event is fired BEFORE the TileEntity is removed from World at given
             * position and also the information about it is send to Player.
             */
            public static class SendInfoToPlayer extends Before {
                private final PlayerEntity player;

                public SendInfoToPlayer(ICustomTileEntity customTileEntity, World world, BlockPos pos, PlayerEntity player) {
                    super(customTileEntity, world, pos);
                    this.player = player;
                }

                /**
                 * @return Returns the Player which will receive an information.
                 */
                public PlayerEntity getPlayerEntity() {
                    return player;
                }
            }
        }

        /**
         * This Event is fired AFTER the TileEntity has been removed from World at given
         * position.<br>
         * ICustomTileEntity is NULL !!!
         */
        public static class After extends Remove {
            public After(ICustomTileEntity customTileEntity, World world, BlockPos pos) {
                super(customTileEntity, world, pos);
            }

            /**
             * This Event is fired AFTER the TileEntity has been removed from World at given
             * position and also the information about it has been send to Player.
             */
            public static class SendInfoToPlayer extends After {
                private final PlayerEntity player;

                public SendInfoToPlayer(ICustomTileEntity customTileEntity, World world, BlockPos pos, PlayerEntity player) {
                    super(customTileEntity, world, pos);
                    this.player = player;
                }

                /**
                 * @return Returns the Player which received an information.
                 */
                public PlayerEntity getPlayerEntity() {
                    return player;
                }
            }
        }
    }
}