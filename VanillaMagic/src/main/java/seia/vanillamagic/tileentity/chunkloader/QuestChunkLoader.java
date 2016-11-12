package seia.vanillamagic.tileentity.chunkloader;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.api.tileentity.ICustomTileEntity;
import seia.vanillamagic.handler.customtileentity.CustomTileEntityHandler;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.util.EntityHelper;
import seia.vanillamagic.util.WorldHelper;

public class QuestChunkLoader extends Quest
{
	@SubscribeEvent
	public void chunkLoaderPlaced(PlaceEvent event)
	{
		BlockPos chunkLoaderPos = event.getPos();
		EntityPlayer placedBy = event.getPlayer();
		ItemStack itemInHand = event.getItemInHand();
		World world = placedBy.worldObj;
		if(itemInHand == null)
		{
			return;
		}
		if(itemInHand.getItem() != null)
		{
			if(Block.isEqualTo(Block.getBlockFromItem(itemInHand.getItem()), Blocks.ENCHANTING_TABLE))
			{
				TileChunkLoader tileChunkLoader = new TileChunkLoader();
				if(isChunkLoaderBuildCorrectly(world, chunkLoaderPos))
				{
					if(canPlayerGetAchievement(placedBy))
					{
						placedBy.addStat(achievement, 1);
					}
					if(placedBy.hasAchievement(achievement))
					{
						tileChunkLoader.init(placedBy, chunkLoaderPos);
						if(CustomTileEntityHandler.addCustomTileEntity(tileChunkLoader, placedBy.dimension))
						{
							EntityHelper.addChatComponentMessage(placedBy, tileChunkLoader.getClass().getSimpleName() + " added");
						}
					}
				}
			}
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
			remove(world, destroyedBlockPos, breakBy);
		}
		else if(Block.isEqualTo(world.getBlockState(destroyedBlockPos).getBlock(), Blocks.TORCH))
		{
			for(EnumFacing face : EnumFacing.values())
			{
				BlockPos chunkLoaderPos = destroyedBlockPos.offset(face);
				if(Block.isEqualTo(world.getBlockState(chunkLoaderPos).getBlock(), Blocks.ENCHANTING_TABLE))
				{
					remove(world, destroyedBlockPos.offset(face), breakBy);
					return;
				}
			}
		}
		else if(Block.isEqualTo(world.getBlockState(destroyedBlockPos).getBlock(), Blocks.OBSIDIAN))
		{
			BlockPos upperPos = new BlockPos(destroyedBlockPos.getX(), destroyedBlockPos.getY() + 1, destroyedBlockPos.getZ());
			chunkLoaderBreak(new BreakEvent(event.getWorld(), upperPos, event.getState(), event.getPlayer()));
		}
	}
	
	private void remove(World world, BlockPos pos, EntityPlayer player)
	{
		ICustomTileEntity customTile = CustomTileEntityHandler.getCustomTileEntity(pos, WorldHelper.getDimensionID(world));
		if(customTile == null)
		{
			return;
		}
		if(CustomTileEntityHandler.removeCustomTileEntityAtPos(world, pos))
		{
			EntityHelper.addChatComponentMessage(player, customTile.getClass().getSimpleName() + " removed");
		}
	}
	
	public static boolean isChunkLoaderBuildCorrectly(World world, BlockPos chunkLoaderPos)
	{
		BlockPos torchTop = new BlockPos(chunkLoaderPos.getX(), chunkLoaderPos.getY(), chunkLoaderPos.getZ() + 1);
		BlockPos torchLeft = new BlockPos(chunkLoaderPos.getX() - 1, chunkLoaderPos.getY(), chunkLoaderPos.getZ());
		BlockPos torchRight = new BlockPos(chunkLoaderPos.getX() + 1, chunkLoaderPos.getY(), chunkLoaderPos.getZ());
		BlockPos torchBottom = new BlockPos(chunkLoaderPos.getX(), chunkLoaderPos.getY(), chunkLoaderPos.getZ() - 1);
		boolean areTorchesCorrrectly = false;
		if(world.getBlockState(torchTop).getBlock() instanceof BlockTorch)
		{
			if(world.getBlockState(torchLeft).getBlock() instanceof BlockTorch)
			{
				if(world.getBlockState(torchRight).getBlock() instanceof BlockTorch)
				{
					if(world.getBlockState(torchBottom).getBlock() instanceof BlockTorch)
					{
						areTorchesCorrrectly = true;
					}
				}
			}
		}
		if(areTorchesCorrrectly)
		{
			BlockPos obsidianUnder = new BlockPos(chunkLoaderPos.getX(), chunkLoaderPos.getY() - 1, chunkLoaderPos.getZ());
			BlockPos obsidianTop = new BlockPos(chunkLoaderPos.getX(), chunkLoaderPos.getY() - 1, chunkLoaderPos.getZ() + 1);
			BlockPos obsidianLeft = new BlockPos(chunkLoaderPos.getX() - 1, chunkLoaderPos.getY() - 1, chunkLoaderPos.getZ());
			BlockPos obsidianRight = new BlockPos(chunkLoaderPos.getX() + 1, chunkLoaderPos.getY() - 1, chunkLoaderPos.getZ());
			BlockPos obsidianBottom = new BlockPos(chunkLoaderPos.getX(), chunkLoaderPos.getY() - 1, chunkLoaderPos.getZ() - 1);
			if(Block.isEqualTo(world.getBlockState(obsidianUnder).getBlock(), Blocks.OBSIDIAN))
			{
				if(Block.isEqualTo(world.getBlockState(obsidianTop).getBlock(), Blocks.OBSIDIAN))
				{
					if(Block.isEqualTo(world.getBlockState(obsidianLeft).getBlock(), Blocks.OBSIDIAN))
					{
						if(Block.isEqualTo(world.getBlockState(obsidianRight).getBlock(), Blocks.OBSIDIAN))
						{
							if(Block.isEqualTo(world.getBlockState(obsidianBottom).getBlock(), Blocks.OBSIDIAN))
							{
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
}