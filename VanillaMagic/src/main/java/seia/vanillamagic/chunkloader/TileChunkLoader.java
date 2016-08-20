package seia.vanillamagic.chunkloader;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.util.ITickable;
import net.minecraft.util.math.ChunkPos;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import seia.vanillamagic.VanillaMagic;
import seia.vanillamagic.utils.BlockPosHelper;
import seia.vanillamagic.utils.CustomTileEntity;

public class TileChunkLoader extends CustomTileEntity implements ITickable
{
	// Name for tile
	public static final String REGISTRY_NAME = TileChunkLoader.class.getSimpleName();
	
	private Ticket chunkTicket;
	
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
	public void update() 
	{
		if(!QuestChunkLoader.isChunkLoaderBuildCorrectly(worldObj, this.pos))
		{
			invalidate();
			System.out.println("Incorrect ChunkLoader placed on:");
			BlockPosHelper.printCoords(this.pos);
		}
	}
}