package seia.vanillamagic.quest.quarry;

import java.io.Serializable;
import java.util.ArrayList;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
	
	@SubscribeEvent
	public void tick(WorldTickEvent event)
	{
		for(Quarry quarry : quarryList)
		{
			World world = quarry.world;
			if(world.getChunkFromBlockCoords(quarry.quarryPos).isLoaded())
			{
				//if(world.getChunkFromBlockCoords(quarry.chestBlockPos).isLoaded())
				{
					//if(world.getChunkFromBlockCoords(quarry.diamondBlockPos).isLoaded())
					{
						if(world.getChunkFromBlockCoords(quarry.getLeftPos()).isLoaded())
						{
							if(world.getChunkFromBlockCoords(quarry.getTopLeftPos()).isLoaded())
							{
								if(world.getChunkFromBlockCoords(quarry.getTopPos()).isLoaded())
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
		}
	}
}