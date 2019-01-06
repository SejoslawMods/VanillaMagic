package seia.vanillamagic.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidBlock;

/**
 * Class which store various methods connected with Block.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class BlockUtil {
	private BlockUtil() {
	}

	/**
	 * @return Returns the number of blocks counted in given direction.
	 */
	public static int countBlocks(World world, BlockPos startPos, Block shouldBe, EnumFacing direction) {
		int count = 0;
		BlockPos next = new BlockPos(startPos.getX(), startPos.getY(), startPos.getZ());
		Block nextBlock = world.getBlockState(next).getBlock();

		while (Block.isEqualTo(nextBlock, shouldBe)) {
			count++;
			next = next.offset(direction);
			nextBlock = world.getBlockState(next).getBlock();
		}

		return count;
	}

	/**
	 * @return Returns true if the given state is a fluid.
	 */
	public static boolean isBlockLiquid(IBlockState state) {
		return (state instanceof IFluidBlock || state.getMaterial().isLiquid());
	}

	/**
	 * Place block from given stack on given position.
	 */
	public static void placeBlockFromStack(World world, BlockPos placePosition, ItemStack stack) {
		Item itemFromStack = stack.getItem();

		if (itemFromStack == null) {
			return;
		}

		Block blockFromStack = Block.getBlockFromItem(itemFromStack);

		if (blockFromStack == null) {
			return;
		}

		world.setBlockState(placePosition, blockFromStack.getDefaultState());
	}
}