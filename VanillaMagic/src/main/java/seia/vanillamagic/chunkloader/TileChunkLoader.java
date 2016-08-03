package seia.vanillamagic.chunkloader;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import seia.vanillamagic.VanillaMagic;

public class TileChunkLoader extends TileEntity
{
	// Name for tile
	public static final String TILE_CHUNK_LOADER_NAME = "tileChunkLoader";
	
	public final EntityPlayer placedBy;
	public final BlockPos position;
	public ItemStack itemInHand;
	
	private Ticket chunkTicket;
	
	public TileChunkLoader(BlockPos chunkLoaderPos, EntityPlayer placedBy, ItemStack itemInHand) 
	{
		this.placedBy = placedBy;
		this.position = chunkLoaderPos;
		this.itemInHand = itemInHand;
	}
	
	public List<ChunkPos> getLoadArea()
	{
		List<ChunkPos> loadArea = new LinkedList();
		ChunkPos chunkCoords = new ChunkPos((this.getPos().getX() >> 4), (this.getPos().getZ() >> 4));
		loadArea.add(chunkCoords);
		return loadArea;
	}
	
	@Override
	public void validate() 
	{
		super.validate();
		if ((!this.worldObj.isRemote) && (this.chunkTicket == null)) 
		{
			Ticket ticket = ForgeChunkManager.requestTicket(VanillaMagic.INSTANCE, this.worldObj, ForgeChunkManager.Type.NORMAL);
			if (ticket != null) 
			{
				forceChunkLoading(ticket);
			}
		}
	}

	@Override
	public void invalidate() 
	{
		super.invalidate();
		stopChunkLoading();
	}

	public void forceChunkLoading(Ticket ticket) 
	{
		//stopChunkLoading();
		this.chunkTicket = ticket;
		for(ChunkPos coord : getLoadArea()) 
		{
			ForgeChunkManager.forceChunk(this.chunkTicket, coord);
		}
	}
	/*
	public void unforceChunkLoading() 
	{
		for(Object obj : this.chunkTicket.getChunkList()) 
		{
			ChunkPos coord = (ChunkPos) obj;
			ForgeChunkManager.unforceChunk(this.chunkTicket, coord);
		}
	}
	*/
	public void stopChunkLoading() 
	{
		if (this.chunkTicket != null) 
		{
			ForgeChunkManager.releaseTicket(this.chunkTicket);
			this.chunkTicket = null;
		}
	}
	
	@Override
	public void readFromNBT(NBTTagCompound par1NBTTagCompound) 
	{
		super.readFromNBT(par1NBTTagCompound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound par1NBTTagCompound) 
	{
		return super.writeToNBT(par1NBTTagCompound);
	}
}