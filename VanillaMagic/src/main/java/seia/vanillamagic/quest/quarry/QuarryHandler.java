package seia.vanillamagic.quest.quarry;

import java.io.Serializable;
import java.util.ArrayList;

import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;
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
	
	@SubscribeEvent
	public void tick(WorldTickEvent event)
	{
		for(Quarry quarry : quarryList)
		{
			quarry.doWork();
		}
	}
}