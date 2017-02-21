package seia.vanillamagic.api.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

public abstract class CustomTileEntityBase extends TileEntity implements ICustomTileEntity
{
	protected Ticket chunkTicket;
	
	public void init(World world, BlockPos pos)
	{
		this.world = world;
		this.pos = pos;
	}
	
	public TileEntity getTileEntity()
	{
		return this;
	}
	
	public void invalidate() 
	{
		super.invalidate();
		stopChunkLoading();
	}
	
	public void stopChunkLoading() 
	{
		if(this.chunkTicket != null) 
		{
			ForgeChunkManager.releaseTicket(this.chunkTicket);
			this.chunkTicket = null;
		}
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
	
	public List<String> getAdditionalInfo()
	{
		List<String> list = new ArrayList<String>();
		list.add("CustomTileEntity name: " + getClass().getSimpleName());
		list.add("CustomTileEntity position: X=" + pos.getX() + ", Y=" + pos.getY() + ", Z=" + pos.getZ());
		return list;
	}
	
	public Ticket getChunkTicket()
	{
		return chunkTicket;
	}
}