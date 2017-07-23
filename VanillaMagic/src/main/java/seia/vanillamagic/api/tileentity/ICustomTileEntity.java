package seia.vanillamagic.api.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.util.INBTSerializable;
import seia.vanillamagic.api.event.EventCustomTileEntity;
import seia.vanillamagic.api.util.IAdditionalInfoProvider;

/**
 * This is the base definition for CustomTileEntity.<br>
 * Each CustomTileEntity is self-chunkloading. <br>
 * <br>
 * If You want to take a CustomTile from any position, methods in World may not work correctly because
 * CustomTile does not have a proper block. In order to take CustomTile You must search through all
 * tickables list and check the position of the tickable if it is the same position as Your wanted tile.
 * This is resource-heavy way but the easiest way. 
 * Other way is that You override the EventCustomTileEntity events and create Your own lists of CustomTiles
 * when it Add or Remove CustomTile to / from World.
 * 
 * @see EventCustomTileEntity
 */
public interface ICustomTileEntity extends 
		ITickable, IAdditionalInfoProvider, INBTSerializable<NBTTagCompound>, 
		ITileEntityWrapper
{
	/**
	 * This initialization will be used for placing the ICustomTileEntity FOR THE FIRST TIME on the right position.<br>
	 * Loading / Saving will be done by {@link INBTSerializable}<br>
	 * Any variables that should be saved must be read / write by {@link INBTSerializable}'s methods.
	 */
	void init(World world, BlockPos pos);
	
	/**
	 * Forcing chunks to be loaded on this {@link Ticket}.
	 */
	void forceChunkLoading(Ticket ticket);
	
	/**
	 * @return Returns the update packet for this CustomTileEntity.
	 */
	SPacketUpdateTileEntity getUpdatePacket();
	
	/**
	 * Used to update this CustomTilenEntity.
	 */
	void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt);
	
	/**
	 * @return Returns the tag compound which is used to update this CustomTileEntity.
	 */
	NBTTagCompound getUpdateTag();
	
	/**
	 * @return Returns the {@link Ticket} for this CustomTileEntity.
	 */
	Ticket getChunkTicket();
}