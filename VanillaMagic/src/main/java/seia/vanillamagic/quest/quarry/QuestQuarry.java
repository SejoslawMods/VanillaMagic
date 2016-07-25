package seia.vanillamagic.quest.quarry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.utils.BlockPosHelper;

public class QuestQuarry extends Quest
{
	public QuestQuarry(Achievement required, int posX, int posY, String questName, String uniqueName) 
	{
		super(required, posX, posY, Item.getItemFromBlock(Blocks.CAULDRON), questName, uniqueName);
	}

	@SubscribeEvent
	public void quarryPlaced(PlaceEvent event)
	{
		BlockPos quarryPos = event.getPos();
		EntityPlayer whoPlacedQuarry = event.getPlayer();
		ItemStack itemInHand = event.getItemInHand();
		try
		{
			if(!whoPlacedQuarry.hasAchievement(achievement))
			{
				whoPlacedQuarry.addStat(achievement, 1);
				return;
			}
			else if(whoPlacedQuarry.hasAchievement(achievement))
			{
				if(Block.getBlockFromItem(itemInHand.getItem()) instanceof BlockCauldron)
				{
					// Should now throw exception if casting in constructor is wrong.
					Quarry quarry = new Quarry(quarryPos, whoPlacedQuarry, itemInHand);
					if(quarry.isComplete())
					{
						quarry.showBoundingBox();
						QuarryHandler.INSTANCE.addNewQuerry(quarry);
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Incorrect quarry placed on:");
			BlockPosHelper.printCoords(quarryPos);
		}
	}

	@SubscribeEvent
	public void quarryBreak(BreakEvent event)
	{
		BlockPos quarryPos = event.getPos();
		try
		{
			QuarryHandler.INSTANCE.removeQuarryFromList(quarryPos);
		}
		catch(Exception e)
		{
			System.out.println("Incorrect quarry broke on:");
			BlockPosHelper.printCoords(quarryPos);
		}
	}
}