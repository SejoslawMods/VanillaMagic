package seia.vanillamagic.quest.quarry;

import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.WorldTickEvent;

public class QuestQuarryEvent
{
	public QuestQuarryEvent() 
	{
		System.out.println("QuarryEvent registered");
	}
	
	@SubscribeEvent
	public void onWorldUnload(WorldEvent.Unload event)
	{
		if(QuarryHandler.INSTANCE.countQuarrys() > 0)
		{
			QuarryHandler.INSTANCE.onWorldUnload();
			System.out.println("QuarryList cleared");
		}
	}
	
	@SubscribeEvent
	public void tick(WorldTickEvent event)
	{
		for(Quarry quarry : QuarryHandler.INSTANCE.quarryList)
		{
			World world = quarry.world;
			if(world.getChunkFromBlockCoords(quarry.quarryPos).isLoaded())
			{
				if(world.getChunkFromBlockCoords(quarry.getDiggingPos()).isLoaded())
				{
					if(quarry.checkSurroundings())
					{
						quarry.showBoundingBox();
						quarry.checkFuel();
						if(quarry.canDig())
						{
							quarry.doWork();
						}
					}
				}
			}
		}
	}
}