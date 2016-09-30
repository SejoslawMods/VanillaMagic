package seia.vanillamagic.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

public interface ICustomTileEntity extends ITickable
{
	void init(EntityPlayer player, BlockPos pos);
	
	void init(World world, BlockPos pos);
	
	void forceChunkLoading(Ticket ticket);
	
	void readFromNBT(NBTTagCompound tag);
	
	NBTTagCompound writeToNBT(NBTTagCompound tag);
	
	SPacketUpdateTileEntity getUpdatePacket();
	
	void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt);
	
	NBTTagCompound getUpdateTag();
	
	Ticket getChunkTicket();
	
	String getPlayerName();
}