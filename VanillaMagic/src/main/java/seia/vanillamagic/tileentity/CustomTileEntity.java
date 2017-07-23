package seia.vanillamagic.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import seia.vanillamagic.api.tileentity.CustomTileEntityBase;
import seia.vanillamagic.core.VanillaMagic;
import seia.vanillamagic.util.NBTUtil;

public abstract class CustomTileEntity extends CustomTileEntityBase
{
	public void validate() 
	{
		super.validate();
		if ((!this.world.isRemote) && (this.chunkTicket == null)) 
		{
			Ticket ticket = ForgeChunkManager.requestTicket(VanillaMagic.INSTANCE, this.world, ForgeChunkManager.Type.NORMAL);
			if (ticket != null) forceChunkLoading(ticket);
		}
	}
	
	/**
	 * Try to override {@link #deserializeNBT(NBTTagCompound)} instead of this method.
	 */
	@Deprecated
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		NBTUtil.readFromINBTSerializable(this, tag);
	}
	
	public void deserializeNBT(NBTTagCompound tag)
	{
	}
	
	/**
	 * Try to override {@link #serializeNBT()} instead of this method.
	 */
	@Deprecated
	public NBTTagCompound writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		tag = NBTUtil.writeToINBTSerializable(this, tag);
		tag.setString(NBTUtil.NBT_CLASS, this.getClass().getName());
		return tag;
	}
	
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound tag = new NBTTagCompound();
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
}