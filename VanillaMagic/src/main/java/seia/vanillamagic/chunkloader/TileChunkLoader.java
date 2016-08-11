package seia.vanillamagic.chunkloader;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.util.INBTSerializable;
import seia.vanillamagic.VanillaMagic;
import seia.vanillamagic.machine.TileMachine;
import seia.vanillamagic.utils.BlockPosHelper;
import seia.vanillamagic.utils.IDimensionKeeper;

public class TileChunkLoader extends TileEntity implements ITickable, IDimensionKeeper
{
	// Name for tile
	public static final String REGISTRY_NAME = "TileChunkLoader";
	
	public EntityPlayer placedBy;
	
	private Ticket chunkTicket;
	private int dimension;
	
	/*
	public TileChunkLoader(BlockPos chunkLoaderPos, EntityPlayer placedBy) 
	{
		this(chunkLoaderPos, placedBy.worldObj);
		this.placedBy = placedBy;
		this.dimension = placedBy.dimension;
	}
	
	public TileChunkLoader(BlockPos chunkLoaderPos, World world) 
	{
		this.worldObj = world;
		this.position = chunkLoaderPos;
	}
	*/
	
	public void init(BlockPos chunkLoaderPos, EntityPlayer placedBy)
	{
		init(chunkLoaderPos, placedBy.worldObj);
		this.placedBy = placedBy;
		this.dimension = placedBy.dimension;
	}
	
	public void init(BlockPos chunkLoaderPos, World world)
	{
		this.worldObj = world;
		this.pos = chunkLoaderPos;
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
	public void readFromNBT(NBTTagCompound tag) 
	{
		super.readFromNBT(tag);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound tag) 
	{
		return super.writeToNBT(tag);
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
	
	public void setDimension(int dimension) 
	{
		this.dimension = dimension;
	}
	
	public int getDimension() 
	{
		return dimension;
	}
	
	public TileEntity getCustomTileEntity() 
	{
		return this;
	}
	
	public NBTTagCompound serializeNBT()
	{
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger(TileMachine.NBT_DIMENSION, dimension);
		return tag;
	}
	
	public void deserializeNBT(NBTTagCompound tag)
	{
		this.dimension = tag.getInteger(TileMachine.NBT_DIMENSION);
	}
}