package seia.vanillamagic.handler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager.OrderedLoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import seia.vanillamagic.api.tileentity.CustomTileEntityBase;
import seia.vanillamagic.api.tileentity.ICustomTileEntity;

/**
 * Class which defines additional chunks loading and what can load them.
 */
public class ChunkLoadingHandler implements OrderedLoadingCallback
{
	/**
	 * Load chunks from given tickets.
	 */
	public void ticketsLoaded(List<Ticket> tickets, World world)
	{
		for (Ticket ticket : tickets)
		{
			NBTTagCompound modData = ticket.getModData();
			int posX = modData.getInteger("x");
			int posY = modData.getInteger("y");
			int posZ = modData.getInteger("z");
			BlockPos pos = new BlockPos(posX, posY, posZ);
			TileEntity tile = world.getTileEntity(pos);
			
			if (tile instanceof CustomTileEntityBase) ((CustomTileEntityBase) tile).forceChunkLoading(ticket);
			else if(tile instanceof ICustomTileEntity) ((ICustomTileEntity) tile).forceChunkLoading(ticket);
		}
	}
	
	/**
	 * Load chunks from given tickets.
	 */
	public List<Ticket> ticketsLoaded(List<Ticket> tickets, World world, int maxTicketCount)
	{
		List<Ticket> validTickets = new ArrayList<Ticket>();
		for (Ticket ticket : tickets)
		{
			NBTTagCompound modData = ticket.getModData();
			int posX = modData.getInteger("x");
			int posY = modData.getInteger("y");
			int posZ = modData.getInteger("z");
			BlockPos pos = new BlockPos(posX, posY, posZ);
			TileEntity tile = world.getTileEntity(pos);
			
			if (tile instanceof CustomTileEntityBase) validTickets.add(ticket);
			else if(tile instanceof ICustomTileEntity) validTickets.add(ticket);
		}
		return validTickets;
	}
}