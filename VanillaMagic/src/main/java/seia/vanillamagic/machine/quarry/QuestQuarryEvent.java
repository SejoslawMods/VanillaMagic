package seia.vanillamagic.machine.quarry;

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
}