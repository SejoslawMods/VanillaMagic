package seia.vanillamagic.chunkloader;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockTorch;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.utils.BlockPosHelper;

public class ChunkLoadingHelper 
{
	public static final ChunkLoadingHelper INSTANCE = new ChunkLoadingHelper();
	
	public final List<TileChunkLoader> chunkLoaders = new ArrayList<TileChunkLoader>();
	
	private ChunkLoadingHelper()
	{
		System.out.println("ChunkLoaderHelper registered");
	}
	
	/*
	 * Just to init the static variables.
	 */
	public void init()
	{
	}
	
	public void onWorldUnload()
	{
		chunkLoaders.clear();
	}
	
	public void addNewChunkLoader(TileChunkLoader tile)
	{
		// Prevent from adding second chunk loader at the same position
		if(getChunkLoaderAtPos(tile.getPos()) == null)
		{
			chunkLoaders.add(tile);
			{
				// Adding to world
				tile.placedBy.worldObj.addTileEntity(tile);
				tile.placedBy.worldObj.setTileEntity(tile.position, tile);
			}
			System.out.println("ChunkLoader registered at:");
			BlockPosHelper.printCoords(tile.getPos());
		}
	}
	
	public void removeChunkLoaderFromList(BlockPos chunkLoaderPos)
	{
		try
		{
			for(int i = 0; i < chunkLoaders.size(); i++)
			{
				TileChunkLoader chunkLoader = chunkLoaders.get(i);
				if(BlockPosHelper.isSameBlockPos(chunkLoaderPos, chunkLoader.position))
				{
					chunkLoaders.remove(i);
					chunkLoader.placedBy.worldObj.removeTileEntity(chunkLoaderPos); // Removing from world
					System.out.println("ChunkLoader removed at:");
					BlockPosHelper.printCoords(chunkLoader.position);
					return;
				}
			}
		}
		catch(Exception e) 
		{
		}
	}
	
	public TileChunkLoader getChunkLoaderAtPos(BlockPos pos)
	{
		for(TileChunkLoader tile : chunkLoaders)
		{
			if(BlockPosHelper.isSameBlockPos(pos, tile.position))
			{
				return tile;
			}
		}
		return null;
	}
	
	public boolean isChunkLoaderBuildCorrectly(World world, BlockPos chunkLoaderPos)
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