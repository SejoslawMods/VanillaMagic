package seia.vanillamagic.machine.quarry;

import java.util.ArrayList;

import net.minecraft.util.math.BlockPos;
import seia.vanillamagic.utils.BlockPosHelper;

public class QuarryHandler
{
	public static final QuarryHandler INSTANCE = new QuarryHandler();
	
	//======================================================================================

	public final ArrayList<TileQuarry> tileQuarryList = new ArrayList<TileQuarry>();

	private QuarryHandler()
	{
		System.out.println("QuarryHandler registered");
	}
	
	// method just to create static INSTANCE
	public void init()
	{
	}
	
	public int countQuarrys()
	{
		return tileQuarryList.size();
	}
	
	public void addNewQuarry(TileQuarry quarry)
	{
		tileQuarryList.add(quarry);
		{
			quarry.getWorld().addTileEntity(quarry);
			quarry.getWorld().setTileEntity(quarry.getMachinePos(), quarry);
		}
		System.out.println("Quarry registered at:");
		BlockPosHelper.printCoords(quarry.getMachinePos());
	}
	
	public void removeQuarryFromList(BlockPos quarryPos)
	{
		try
		{
			for(int i = 0; i < tileQuarryList.size(); i++)
			{
				TileQuarry quarry = tileQuarryList.get(i);
				if(BlockPosHelper.isSameBlockPos(quarryPos, quarry.getMachinePos()))
				{
					tileQuarryList.remove(i);
					quarry.getWorld().removeTileEntity(quarryPos);
					System.out.println("Quarry removed at:");
					BlockPosHelper.printCoords(quarry.getMachinePos());
					return;
				}
			}
		}
		catch(Exception e) 
		{
		}
	}
	
	public void killQuarry(TileQuarry quarry)
	{
		removeQuarryFromList(quarry.getMachinePos());
	}
}