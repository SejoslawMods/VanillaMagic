package seia.vanillamagic.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;

public class BlockHelper 
{
	private BlockHelper()
	{
	}
	
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
	
	public static boolean isBlockLiquid(IBlockState state)
	{
		return (state instanceof IFluidBlock || state.getMaterial().isLiquid());
	}

	public static void placeBlockFromStack(World world, BlockPos placePosition, ItemStack stack) 
	{
		Item itemFromStack = stack.getItem();
		if(itemFromStack != null)
		{
			Block blockFromStack = Block.getBlockFromItem(itemFromStack);
			if(blockFromStack != null)
			{
				world.setBlockState(placePosition, blockFromStack.getDefaultState());
			}
		}
	}
}