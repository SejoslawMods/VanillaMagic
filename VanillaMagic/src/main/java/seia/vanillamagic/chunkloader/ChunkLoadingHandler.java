package seia.vanillamagic.chunkloader;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

public class ChunkLoadingHandler implements LoadingCallback 
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
			if(te instanceof TileChunkLoader)
			{
				((TileChunkLoader) te).forceChunkLoading(ticket);
			}
		}
	}
}