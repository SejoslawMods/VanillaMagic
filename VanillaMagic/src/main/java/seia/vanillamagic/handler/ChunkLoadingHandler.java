package seia.vanillamagic.handler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager.OrderedLoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import seia.vanillamagic.tileentity.CustomTileEntity;

public class ChunkLoadingHandler implements OrderedLoadingCallback
{
	public void ticketsLoaded(List<Ticket> tickets, World world)
	{
		for(Ticket ticket : tickets)
		{
			NBTTagCompound modData = ticket.getModData();
			int posX = modData.getInteger("x");
			int posY = modData.getInteger("y");
			int posZ = modData.getInteger("z");
			BlockPos pos = new BlockPos(posX, posY, posZ);
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof CustomTileEntity)
			{
				((CustomTileEntity) te).forceChunkLoading(ticket);
			}
		}
	}
	
	public List<Ticket> ticketsLoaded(List<Ticket> tickets, World world, int maxTicketCount)
	{
		List<Ticket> validTickets = new ArrayList<Ticket>();
		for(Ticket ticket : tickets)
		{
			NBTTagCompound modData = ticket.getModData();
			int posX = modData.getInteger("x");
			int posY = modData.getInteger("y");
			int posZ = modData.getInteger("z");
			BlockPos pos = new BlockPos(posX, posY, posZ);
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof CustomTileEntity)
			{
				validTickets.add(ticket);
			}
		}
		return validTickets;
	}
}