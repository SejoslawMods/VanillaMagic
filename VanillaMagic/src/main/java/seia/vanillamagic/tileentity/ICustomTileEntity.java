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
	public void init(EntityPlayer player, BlockPos pos);
	
	public void init(World world, BlockPos pos);
	
	public void forceChunkLoading(Ticket ticket);
	
	public void readFromNBT(NBTTagCompound tag);
	
	public NBTTagCompound writeToNBT(NBTTagCompound tag);
	
	public SPacketUpdateTileEntity getUpdatePacket();
	
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt);
	
	public NBTTagCompound getUpdateTag();
	
	public Ticket getChunkTicket();
	
	public String getPlayerName();
}