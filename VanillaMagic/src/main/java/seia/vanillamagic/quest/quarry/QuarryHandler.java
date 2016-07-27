package seia.vanillamagic.quest.quarry;

import java.io.Serializable;
import java.util.ArrayList;

import net.minecraft.util.math.BlockPos;
import seia.vanillamagic.utils.BlockPosHelper;

public class QuarryHandler implements Serializable
{
	public static final QuarryHandler INSTANCE = new QuarryHandler();
	
	//======================================================================================
	
	public final ArrayList<Quarry> quarryList;

	public QuarryHandler()
	{
		quarryList = new ArrayList<Quarry>();
		System.out.println("QuarryHandler registered");
	}
	
	// method just to create static INSTANCE
	public void init()
	{
	}
	
	public void onWorldUnload()
	{
		quarryList.clear();
	}
	
	public int countQuarrys()
	{
		return quarryList.size();
	}

	public void addNewQuarry(Quarry quarry)
	{
		quarryList.add(quarry);
		System.out.println("Quarry registered at:");
		BlockPosHelper.printCoords(quarry.quarryPos);
	}
	
	public void removeQuarryFromList(BlockPos quarryPos)
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
	
	public void killQuarry(Quarry quarry)
	{
		removeQuarryFromList(quarry.quarryPos);
	}
}