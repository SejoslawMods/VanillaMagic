package seia.vanillamagic.utils;

import net.minecraft.block.Block;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockHelper 
{
	public static int countBlocks(World world, BlockPos startPos, Block shouldBe, EnumFacing direction) 
	{
		int count = 0;
		BlockPos next = new BlockPos(startPos.getX(), startPos.getY(), startPos.getZ());
		Block nextBlock = world.getBlockState(next).getBlock();
		while(Block.isEqualTo(nextBlock, shouldBe))
		{
			count++;
			next = next.offset(direction);
			nextBlock = world.getBlockState(next).getBlock();
		}
		return count;
	}
}