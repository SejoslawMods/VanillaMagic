package seia.vanillamagic.quest.quarry;

import java.io.File;
import java.util.ArrayList;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.utils.BlockPosHelper;

public class QuarryHandler implements Runnable
{
	public static final QuarryHandler INSTANCE = new QuarryHandler();
	
	//======================================================================================
	
	public final ArrayList<Quarry> quarryList;
	public final Thread thread;
	public final File file;
	
	public QuarryHandler()
	{
		quarryList = new ArrayList<Quarry>();
		thread = new Thread(this);
		file = new File("vanilla_magic_quarrys_coords");
	}

	public void addNewQuerry(Quarry quarry)
	{
		quarryList.add(quarry);
	}
	
	public void removeQuarryFromList(BlockPos quarryPos)
	{
		for(int i = 0; i < quarryList.size(); i++)
		{
			Quarry quarry = quarryList.get(i);
			if(BlockPosHelper.isSameBlockPos(quarryPos, quarry.quarryPos))
			{
				quarryList.remove(i);
				return;
			}
		}
	}
	
	/*
	 * TODO:
	 * Load Quarrys Coords from file
	 * thread.start()
	 */
	@SubscribeEvent
	public void onWorldLoad(WorldEvent.Load event)
	{
	}
	
	/*
	 * TODO:
	 * Save Quarrys Coords to file
	 */
	@SubscribeEvent
	public void onWorldSave(WorldEvent.Save event)
	{
	}
	
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event)
	{
	}
	
	//TODO:
	@Override
	public void run() 
	{
	}
}