package seia.vanillamagic.api.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.util.INBTSerializable;
import seia.vanillamagic.api.util.IAdditionalInfoProvider;

/**
 * This is the base definition for CustomTileEntity.<br>
 * Each CustomTileEntity is self-chunkloading.
 */
public interface ICustomTileEntity extends 
		ITickable, IAdditionalInfoProvider, INBTSerializable<NBTTagCompound>, 
		ITileEntityWrapper
{
	/**
	 * This initialization is used when Player place a {@link TileEntity} for the first time.
	 */
	void init(EntityPlayer player, BlockPos pos);
	
	/**
	 * This initialization will be used for placing the ICustomTileEntity on the right {@link World} on the right position.
	 */
	void init(World world, BlockPos pos);
	
	/**
	 * Forcing chunks to be loaded on this {@link Ticket}.
	 */
	void forceChunkLoading(Ticket ticket);
	
	SPacketUpdateTileEntity getUpdatePacket();
	
	void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt);
	
	NBTTagCompound getUpdateTag();
	
	/**
	 * Returns the {@link Ticket} for this CustomTileEntity.
	 */
	Ticket getChunkTicket();
	
	/**
	 * Returns the name of the Player who placed this CustomTileEntity.
	 */
	String getPlayerName();
}