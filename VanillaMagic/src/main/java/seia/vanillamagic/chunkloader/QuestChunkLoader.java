package seia.vanillamagic.chunkloader;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.utils.BlockPosHelper;

public class QuestChunkLoader extends Quest
{
	public QuestChunkLoader(Quest required, int posX, int posY, ItemStack icon, String questName, String uniqueName) 
	{
		super(required, posX, posY, icon, questName, uniqueName);
	}
	
	@SubscribeEvent
	public void chunkLoaderPlaced(PlaceEvent event)
	{
		BlockPos chunkLoaderPos = event.getPos();
		EntityPlayer placedBy = event.getPlayer();
		ItemStack itemInHand = event.getItemInHand();
		World world = placedBy.worldObj;
		try
		{
			if(Block.isEqualTo(Block.getBlockFromItem(itemInHand.getItem()), Blocks.ENCHANTING_TABLE))
			{
				TileChunkLoader tileChunkLoader = new TileChunkLoader(chunkLoaderPos, placedBy, itemInHand);
				if(ChunkLoadingHelper.INSTANCE.isChunkLoaderBuildCorrectly(world, tileChunkLoader.position))
				{
					if(!placedBy.hasAchievement(achievement))
					{
						placedBy.addStat(achievement, 1);
					}
					if(placedBy.hasAchievement(achievement))
					{
						ChunkLoadingHelper.INSTANCE.addNewChunkLoader(tileChunkLoader);
					}
				}
			}
		}
		catch(Exception e)
		{
//			System.out.println("Incorrect ChunkLoader placed on:");
//			BlockPosHelper.printCoords(chunkLoaderPos);
		}
	}
	
	@SubscribeEvent
	public void chunkLoaderBreak(BreakEvent event)
	{
		BlockPos destroyedBlockPos = event.getPos();
		EntityPlayer breakBy = event.getPlayer();
		World world = breakBy.worldObj;
		if(Block.isEqualTo(world.getBlockState(destroyedBlockPos).getBlock(), Blocks.ENCHANTING_TABLE))
		{
			if(ChunkLoadingHelper.INSTANCE.getChunkLoaderAtPos(destroyedBlockPos) != null)
			{
				ChunkLoadingHelper.INSTANCE.removeChunkLoaderFromList(destroyedBlockPos);
			}
		}
		else if(Block.isEqualTo(world.getBlockState(destroyedBlockPos).getBlock(), Blocks.TORCH))
		{
			for(EnumFacing face : EnumFacing.values())
			{
				BlockPos chunkLoaderPos = destroyedBlockPos.offset(face);
				if(Block.isEqualTo(world.getBlockState(chunkLoaderPos).getBlock(), Blocks.ENCHANTING_TABLE))
				{
					if(ChunkLoadingHelper.INSTANCE.getChunkLoaderAtPos(chunkLoaderPos) != null)
					{
						ChunkLoadingHelper.INSTANCE.removeChunkLoaderFromList(chunkLoaderPos);
						return;
					}
				}
			}
		}
		else if(Block.isEqualTo(world.getBlockState(destroyedBlockPos).getBlock(), Blocks.OBSIDIAN))
		{
			BlockPos upperPos = new BlockPos(destroyedBlockPos.getX(), destroyedBlockPos.getY() + 1, destroyedBlockPos.getZ());
			chunkLoaderBreak(new BreakEvent(event.getWorld(), upperPos, event.getState(), event.getPlayer()));
		}
	}
}