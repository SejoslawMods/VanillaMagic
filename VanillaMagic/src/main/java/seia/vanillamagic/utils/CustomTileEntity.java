package seia.vanillamagic.utils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

public abstract class CustomTileEntity extends TileEntity implements ITickable
{
	protected Ticket chunkTicket;
	
	public void init(World world, BlockPos pos)
	{
		this.worldObj = world;
		this.pos = pos;
	}
	
	public void forceChunkLoading(Ticket ticket)
	{
		if(chunkTicket == null)
		{
			chunkTicket = ticket;
		}
		ChunkPos tilePos = new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
		ForgeChunkManager.forceChunk(ticket, tilePos);
	}
	
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		NBTTagCompound nbt = new NBTTagCompound();
		this.writeToNBT(nbt);
		return new SPacketUpdateTileEntity(getPos(), -999, nbt);
	}
    
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		super.onDataPacket(net, pkt);
		this.readFromNBT(pkt.getNbtCompound());
	}
	
	public NBTTagCompound getUpdateTag()
	{
		return writeToNBT(new NBTTagCompound());
	}
}