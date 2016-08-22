package seia.vanillamagic.machine.quarry;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager.OrderedLoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

public class QuarryChunkLoadingHandler implements OrderedLoadingCallback
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
			if(te instanceof TileQuarry)
			{
				((TileQuarry) te).forceChunkLoading(ticket);
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
			if(te instanceof TileQuarry)
			{
				validTickets.add(ticket);
			}
		}
		return validTickets;
	}
}