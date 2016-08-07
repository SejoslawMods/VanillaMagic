package seia.vanillamagic.machine.quarry;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.utils.BlockPosHelper;

public class QuestQuarry extends Quest
{
	public QuestQuarry(Quest required, int posX, int posY, String questName, String uniqueName) 
	{
		super(required, posX, posY, new ItemStack(Items.CAULDRON), questName, uniqueName);
	}

	@SubscribeEvent
	public void quarryPlaced(PlaceEvent event)
	{
		BlockPos quarryPos = event.getPos();
		EntityPlayer whoPlacedQuarry = event.getPlayer();
		ItemStack itemInHand = event.getItemInHand();
		World world = whoPlacedQuarry.worldObj;
		try
		{
			if(itemInHand.getItem().equals(Items.CAULDRON))
			{
				// Should now throw exception from constructor if blocks are wrong.
				//Quarry quarry = new Quarry(quarryPos, whoPlacedQuarry, itemInHand);
				TileQuarry tileQuarry = new TileQuarry(quarryPos, whoPlacedQuarry, itemInHand);
				if(tileQuarry.isComplete())
				{
					if(!whoPlacedQuarry.hasAchievement(achievement))
					{
						whoPlacedQuarry.addStat(achievement, 1);
					}
					if(whoPlacedQuarry.hasAchievement(achievement))
					{
						tileQuarry.showBoundingBox();
						QuarryHandler.INSTANCE.addNewQuarry(tileQuarry);
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
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
			else if(Block.isEqualTo(quarryBlock, Blocks.REDSTONE_BLOCK))
			{
				cauldronPos = new BlockPos(quarryPos.getX(), quarryPos.getY(), quarryPos.getZ() + 1);
			}
			else if(Block.isEqualTo(quarryBlock, Blocks.DIAMOND_BLOCK))
			{
				cauldronPos = new BlockPos(quarryPos.getX() - 1, quarryPos.getY(), quarryPos.getZ());
			}
			
			if(cauldronPos != null)
			{
				BlockPos redstonePos = new BlockPos(quarryPos.getX(), quarryPos.getY(), quarryPos.getZ() - 1);
				Block redstoneBlock = world.getBlockState(redstonePos).getBlock();
				if(Block.isEqualTo(redstoneBlock, Blocks.REDSTONE_BLOCK))
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