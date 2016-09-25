package seia.vanillamagic.tileentity;

import net.minecraft.entity.player.EntityPlayer;
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
import seia.vanillamagic.utils.NBTHelper;

public abstract class CustomTileEntity extends TileEntity implements ICustomTileEntity
{
	protected Ticket chunkTicket;
	protected String playerName;
	
	public void init(EntityPlayer player, BlockPos pos)
	{
		this.init(player.worldObj, pos);
		this.playerName = player.getDisplayNameString();
	}
	
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
	
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		this.playerName = tag.getString(NBTHelper.NBT_PLAYER_NAME);
	}
	
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		tag.setString(NBTHelper.NBT_PLAYER_NAME, playerName);
		return tag;
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
	
	public Ticket getChunkTicket()
	{
		return chunkTicket;
	}
	
	public String getPlayerName()
	{
		return playerName;
	}
}