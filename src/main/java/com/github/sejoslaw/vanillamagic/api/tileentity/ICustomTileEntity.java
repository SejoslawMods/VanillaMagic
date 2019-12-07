package com.github.sejoslaw.vanillamagic.api.tileentity;

import com.github.sejoslaw.vanillamagic.api.event.EventCustomTileEntity;
import com.github.sejoslaw.vanillamagic.api.util.IAdditionalInfoProvider;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.Ticket;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * This is the base definition for CustomTileEntity.<br>
 * Each CustomTileEntity is self-chunkloading. <br>
 * <br>
 * If You want to take a CustomTile from any position, methods in World may not
 * work correctly because CustomTile does not have a proper block. In order to
 * take CustomTile You must search through all tickables list and check the
 * position of the tickable if it is the same position as Your wanted tile. This
 * is resource-heavy way but the easiest way. Other way is that You override the
 * EventCustomTileEntity events and create Your own lists of CustomTiles when it
 * Add or Remove CustomTile to / from World.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 * @see EventCustomTileEntity
 */
public interface ICustomTileEntity extends ITickable, IAdditionalInfoProvider, INBTSerializable<CompoundNBT>, ITileEntityWrapper {
    /**
     * This initialization will be used for placing the ICustomTileEntity FOR THE
     * FIRST TIME on the right position.<br>
     * Loading / Saving will be done by {@link INBTSerializable}<br>
     * Any variables that should be saved must be read / write by
     * {@link INBTSerializable}'s methods.
     */
    void init(World world, BlockPos pos);

    /**
     * Forcing chunks to be loaded on this {@link Ticket}.
     */
    void forceChunkLoading(Ticket ticket);

    /**
     * @return Returns the {@link Ticket} for this CustomTileEntity.
     */
    Ticket getChunkTicket();
}