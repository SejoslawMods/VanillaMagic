package seia.vanillamagic.quest.quarry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
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
		EntityPlayer player = event.getPlayer();
		World world = player.worldObj;
		Block quarryBlock = world.getBlockState(quarryPos).getBlock();
		try
		{
			BlockPos cauldronPos = null;
			if(quarryBlock instanceof BlockCauldron)
			{
				cauldronPos = quarryPos;
			}
			else if(quarryBlock instanceof BlockChest)
			{
				cauldronPos = new BlockPos(quarryPos.getX(), quarryPos.getY(), quarryPos.getZ() + 1);
			}
			else if(Block.isEqualTo(quarryBlock, Blocks.DIAMOND_BLOCK))
			{
				cauldronPos = new BlockPos(quarryPos.getX() - 1, quarryPos.getY(), quarryPos.getZ());
			}
			
			if(cauldronPos != null)
			{
				BlockPos chestPos = new BlockPos(quarryPos.getX(), quarryPos.getY(), quarryPos.getZ() - 1);
				Block chestBlock = world.getBlockState(chestPos).getBlock();
				if(chestBlock instanceof BlockChest)
				{
					BlockPos diamondBlockPos = new BlockPos(quarryPos.getX() + 1, quarryPos.getY(), quarryPos.getZ());
					Block diamondBlock = world.getBlockState(diamondBlockPos).getBlock();
					if(Block.isEqualTo(diamondBlock, Blocks.DIAMOND_BLOCK))
					{
						QuarryHandler.INSTANCE.removeQuarryFromList(cauldronPos);
						return;
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println("Incorrect quarry broke on:");
			BlockPosHelper.printCoords(quarryBlock, quarryPos);
		}
	}
}