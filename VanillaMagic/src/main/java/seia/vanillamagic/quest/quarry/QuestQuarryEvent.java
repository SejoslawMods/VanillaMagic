package seia.vanillamagic.quest.quarry;

import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
	public void onWorldLoad(WorldEvent.Load event)
	{
		World world = event.getWorld();
		List<TileEntity> loadedTileEntities = world.tickableTileEntities;
		for(TileEntity tile : loadedTileEntities)
		{
			if(tile instanceof TileQuarry)
			{
				QuarryHandler.INSTANCE.addNewQuarry((TileQuarry)tile);
			}
		}
	}
	
//	@SubscribeEvent
//	public void tick(WorldTickEvent event)
//	{
		/*
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
						if(quarry.isNextToInventory())
						{
							if(quarry.inventoryHasSpace())
							{
								quarry.doWork();
							}
						}
						else
						{
							quarry.doWork();
						}
					}
				}
			}
		}
		*/
		/*
		for(TileQuarry quarry : QuarryHandler.INSTANCE.tileQuarryList)
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
						if(quarry.isNextToInventory())
						{
							if(quarry.inventoryHasSpace())
							{
								quarry.doWork();
							}
						}
						else
						{
							quarry.doWork();
						}
					}
				}
			}
		}
		*/
//	}
}