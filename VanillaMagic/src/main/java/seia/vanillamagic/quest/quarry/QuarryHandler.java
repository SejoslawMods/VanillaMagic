package seia.vanillamagic.quest.quarry;

import java.util.ArrayList;

import net.minecraft.util.math.BlockPos;
import seia.vanillamagic.utils.BlockPosHelper;

public class QuarryHandler
{
	public static final QuarryHandler INSTANCE = new QuarryHandler();
	
	//======================================================================================
	
	//public final ArrayList<Quarry> quarryList;
	public final ArrayList<TileQuarry> tileQuarryList = new ArrayList<TileQuarry>();

	private QuarryHandler()
	{
		//quarryList = new ArrayList<Quarry>();
		System.out.println("QuarryHandler registered");
	}
	
	// method just to create static INSTANCE
	public void init()
	{
	}
	
	public int countQuarrys()
	{
		//return quarryList.size();
		return tileQuarryList.size();
	}
	/*
	public void addNewQuarry(Quarry quarry)
	{
		quarryList.add(quarry);
		System.out.println("Quarry registered at:");
		BlockPosHelper.printCoords(quarry.quarryPos);
	}
	*/
	public void addNewQuarry(TileQuarry quarry)
	{
		tileQuarryList.add(quarry);
		{
			// Adding to world
			quarry.placedBy.worldObj.addTileEntity(quarry);
			quarry.placedBy.worldObj.setTileEntity(quarry.quarryPos, quarry);
		}
		System.out.println("Quarry registered at:");
		BlockPosHelper.printCoords(quarry.quarryPos);
	}
	
	public void removeQuarryFromList(BlockPos quarryPos)
	{
		/*
		try
		{
			for(int i = 0; i < quarryList.size(); i++)
			{
				Quarry quarry = quarryList.get(i);
				if(BlockPosHelper.isSameBlockPos(quarryPos, quarry.quarryPos))
				{
					quarryList.remove(i);
					System.out.println("Quarry removed at:");
					BlockPosHelper.printCoords(quarry.quarryPos);
					return;
				}
			}
		}
		catch(Exception e) 
		{
		}
		*/
		try
		{
			for(int i = 0; i < tileQuarryList.size(); i++)
			{
				TileQuarry quarry = tileQuarryList.get(i);
				if(BlockPosHelper.isSameBlockPos(quarryPos, quarry.quarryPos))
				{
					tileQuarryList.remove(i);
					quarry.placedBy.worldObj.removeTileEntity(quarryPos); // Removing from world
					System.out.println("Quarry removed at:");
					BlockPosHelper.printCoords(quarry.quarryPos);
					return;
				}
			}
		}
		catch(Exception e) 
		{
		}
	}
	/*
	public void killQuarry(Quarry quarry)
	{
		removeQuarryFromList(quarry.quarryPos);
	}
	*/
	public void killQuarry(TileQuarry quarry)
	{
		removeQuarryFromList(quarry.quarryPos);
	}
}