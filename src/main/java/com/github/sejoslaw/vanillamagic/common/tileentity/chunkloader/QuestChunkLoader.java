package com.github.sejoslaw.vanillamagic.tileentity.chunkloader;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.github.sejoslaw.vanillamagic.handler.CustomTileEntityHandler;
import com.github.sejoslaw.vanillamagic.quest.Quest;
import com.github.sejoslaw.vanillamagic.util.EntityUtil;
import com.github.sejoslaw.vanillamagic.util.ItemStackUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestChunkLoader extends Quest {
	@SubscribeEvent
	public void chunkLoaderPlaced(PlaceEvent event) {
		BlockPos chunkLoaderPos = event.getPos();
		PlayerEntity placedBy = event.getPlayer();
		ItemStack itemInHand = event.getItemInHand();
		World world = placedBy.world;

		if (ItemStackUtil.isNullStack(itemInHand) || (itemInHand.getItem() == null)
				|| !BlockUtil.areEqual(Block.getBlockFromItem(itemInHand.getItem()), Blocks.ENCHANTING_TABLE)) {
			return;
		}

		TileChunkLoader tileChunkLoader = new TileChunkLoader();

		if (!isChunkLoaderBuildCorrectly(world, chunkLoaderPos)) {
			return;
		}

		this.checkQuestProgress(placedBy);

		if (!hasQuest(placedBy)) {
			return;
		}

		tileChunkLoader.init(placedBy.world, chunkLoaderPos);

		if (CustomTileEntityHandler.addCustomTileEntity(tileChunkLoader, placedBy.dimension)) {
			EntityUtil.addChatComponentMessageNoSpam(placedBy, tileChunkLoader.getClass().getSimpleName() + " added");
		}
	}

	@SubscribeEvent
	public void chunkLoaderBreak(BreakEvent event) {
		BlockPos destroyedBlockPos = event.getPos();
		PlayerEntity breakBy = event.getPlayer();
		World world = breakBy.world;

		if (BlockUtil.areEqual(world.getBlockState(destroyedBlockPos).getBlock(), Blocks.ENCHANTING_TABLE)) {
			CustomTileEntityHandler.removeCustomTileEntityAndSendInfoToPlayer(world, destroyedBlockPos, breakBy);
		} else if (BlockUtil.areEqual(world.getBlockState(destroyedBlockPos).getBlock(), Blocks.TORCH)) {
			for (Direction face : Direction.values()) {
				BlockPos chunkLoaderPos = destroyedBlockPos.offset(face);

				if (BlockUtil.areEqual(world.getBlockState(chunkLoaderPos).getBlock(), Blocks.ENCHANTING_TABLE)) {
					CustomTileEntityHandler.removeCustomTileEntityAndSendInfoToPlayer(world,
							destroyedBlockPos.offset(face), breakBy);
					return;
				}
			}
		} else if (BlockUtil.areEqual(world.getBlockState(destroyedBlockPos).getBlock(), Blocks.OBSIDIAN)) {
			BlockPos upperPos = new BlockPos(destroyedBlockPos.getX(), destroyedBlockPos.getY() + 1,
					destroyedBlockPos.getZ());
			try {
				// TODO: It crashes sometimes
				// Don't know how to convert minecraft:stone[variant=stone] back into data...
				chunkLoaderBreak(new BreakEvent(event.getWorld(), upperPos, event.getState(), event.getPlayer()));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
	}

	public static boolean isChunkLoaderBuildCorrectly(World world, BlockPos chunkLoaderPos) {
		BlockPos torchTop = new BlockPos(chunkLoaderPos.getX(), chunkLoaderPos.getY(), chunkLoaderPos.getZ() + 1);
		BlockPos torchLeft = new BlockPos(chunkLoaderPos.getX() - 1, chunkLoaderPos.getY(), chunkLoaderPos.getZ());
		BlockPos torchRight = new BlockPos(chunkLoaderPos.getX() + 1, chunkLoaderPos.getY(), chunkLoaderPos.getZ());
		BlockPos torchBottom = new BlockPos(chunkLoaderPos.getX(), chunkLoaderPos.getY(), chunkLoaderPos.getZ() - 1);
		boolean areTorchesCorrectly = false;

		if ((world.getBlockState(torchTop).getBlock() instanceof BlockTorch)
				&& (world.getBlockState(torchLeft).getBlock() instanceof BlockTorch)
				&& (world.getBlockState(torchRight).getBlock() instanceof BlockTorch)
				&& (world.getBlockState(torchBottom).getBlock() instanceof BlockTorch)) {
			areTorchesCorrectly = true;
		}

		if (!areTorchesCorrectly) {
			return false;
		}

		BlockPos obsidianUnder = new BlockPos(chunkLoaderPos.getX(), chunkLoaderPos.getY() - 1, chunkLoaderPos.getZ());
		BlockPos obsidianTop = new BlockPos(chunkLoaderPos.getX(), chunkLoaderPos.getY() - 1,
				chunkLoaderPos.getZ() + 1);
		BlockPos obsidianLeft = new BlockPos(chunkLoaderPos.getX() - 1, chunkLoaderPos.getY() - 1,
				chunkLoaderPos.getZ());
		BlockPos obsidianRight = new BlockPos(chunkLoaderPos.getX() + 1, chunkLoaderPos.getY() - 1,
				chunkLoaderPos.getZ());
		BlockPos obsidianBottom = new BlockPos(chunkLoaderPos.getX(), chunkLoaderPos.getY() - 1,
				chunkLoaderPos.getZ() - 1);

		if (BlockUtil.areEqual(world.getBlockState(obsidianUnder).getBlock(), Blocks.OBSIDIAN)
				&& BlockUtil.areEqual(world.getBlockState(obsidianTop).getBlock(), Blocks.OBSIDIAN)
				&& BlockUtil.areEqual(world.getBlockState(obsidianLeft).getBlock(), Blocks.OBSIDIAN)
				&& BlockUtil.areEqual(world.getBlockState(obsidianRight).getBlock(), Blocks.OBSIDIAN)
				&& BlockUtil.areEqual(world.getBlockState(obsidianBottom).getBlock(), Blocks.OBSIDIAN)) {
			return true;
		}

		return false;
	}
}