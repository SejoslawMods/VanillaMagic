package seia.vanillamagic.util;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Class which is used for checking if the Altar was created successfully.
 * Checking structure, etc.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class AltarUtil {
	public static final Block BLOCK_TIER_1 = Blocks.REDSTONE_WIRE;
	public static final Block BLOCK_TIER_2 = Blocks.IRON_BLOCK;
	public static final Block BLOCK_TIER_3 = Blocks.GOLD_BLOCK;
	public static final Block BLOCK_TIER_4 = Blocks.REDSTONE_BLOCK;
	public static final Block BLOCK_TIER_5 = Blocks.LAPIS_BLOCK;
	public static final Block BLOCK_TIER_6 = Blocks.DIAMOND_BLOCK;
	public static final Block BLOCK_TIER_7 = Blocks.EMERALD_BLOCK;

	private AltarUtil() {
	}

	public static Block getBlockByTier(int tier) {
		switch (tier) {
		case 1:
			return BLOCK_TIER_1;
		case 2:
			return BLOCK_TIER_2;
		case 3:
			return BLOCK_TIER_3;
		case 4:
			return BLOCK_TIER_4;
		case 5:
			return BLOCK_TIER_5;
		case 6:
			return BLOCK_TIER_6;
		case 7:
			return BLOCK_TIER_7;
		default:
			return null;
		}
	}

	public static boolean checkAltarTier(World world, BlockPos midPos, int tier) {
		switch (tier) {
		case 1:
			return checkTier1(world, midPos);
		case 2:
			return checkTier2(world, midPos);
		case 3:
			return checkTier3(world, midPos);
		case 4:
			return checkTier4(world, midPos);
		case 5:
			return checkTier5(world, midPos);
		case 6:
			return checkTier6(world, midPos);
		case 7:
			return checkTier7(world, midPos);
		default:
			return false;
		}
	}

	/**
	 * Cauldron + redstone around
	 */
	public static boolean checkTier1(World world, BlockPos midPos) {
		BlockPos right = new BlockPos(midPos.getX() + 1, midPos.getY(), midPos.getZ());
		BlockPos rightUp = new BlockPos(midPos.getX() + 1, midPos.getY(), midPos.getZ() + 1);
		BlockPos up = new BlockPos(midPos.getX(), midPos.getY(), midPos.getZ() + 1);
		BlockPos leftUp = new BlockPos(midPos.getX() - 1, midPos.getY(), midPos.getZ() + 1);
		BlockPos left = new BlockPos(midPos.getX() - 1, midPos.getY(), midPos.getZ());
		BlockPos leftDown = new BlockPos(midPos.getX() - 1, midPos.getY(), midPos.getZ() - 1);
		BlockPos down = new BlockPos(midPos.getX(), midPos.getY(), midPos.getZ() - 1);
		BlockPos rightDown = new BlockPos(midPos.getX() + 1, midPos.getY(), midPos.getZ() - 1);

		return Block.isEqualTo(world.getBlockState(right).getBlock(), BLOCK_TIER_1)
				&& Block.isEqualTo(world.getBlockState(rightUp).getBlock(), BLOCK_TIER_1)
				&& Block.isEqualTo(world.getBlockState(up).getBlock(), BLOCK_TIER_1)
				&& Block.isEqualTo(world.getBlockState(leftUp).getBlock(), BLOCK_TIER_1)
				&& Block.isEqualTo(world.getBlockState(left).getBlock(), BLOCK_TIER_1)
				&& Block.isEqualTo(world.getBlockState(leftDown).getBlock(), BLOCK_TIER_1)
				&& Block.isEqualTo(world.getBlockState(down).getBlock(), BLOCK_TIER_1)
				&& Block.isEqualTo(world.getBlockState(rightDown).getBlock(), BLOCK_TIER_1);
	}

	/**
	 * Iron on corners
	 */
	public static boolean checkTier2(World world, BlockPos midPos) {
		if (checkTier1(world, midPos)) {
			return checkTierNSidesOnly(world, midPos, 2, BLOCK_TIER_2);
		}

		return false;
	}

	/**
	 * Gold blocks 2 blocks away from cauldron up, down, left, right
	 */
	public static boolean checkTier3(World world, BlockPos midPos) {
		if (checkTier2(world, midPos)) {
			return checkTierNCornersOnly(world, midPos, 3, BLOCK_TIER_3);
		}

		return false;
	}

	/**
	 * Redstone on corners after iron blocks
	 */
	public static boolean checkTier4(World world, BlockPos midPos) {
		if (checkTier3(world, midPos)) {
			return checkTierNSidesOnly(world, midPos, 3, BLOCK_TIER_4);
		}

		return false;
	}

	/**
	 * Lapis blocks 4 blocks away from cauldron up, down, left, right
	 */
	public static boolean checkTier5(World world, BlockPos midPos) {
		if (checkTier4(world, midPos)) {
			return checkTierNCornersOnly(world, midPos, 5, BLOCK_TIER_5);
		}

		return false;
	}

	/**
	 * Diamond blocks on corners after redstone blocks
	 */
	public static boolean checkTier6(World world, BlockPos midPos) {
		if (checkTier5(world, midPos)) {
			return checkTierNSidesOnly(world, midPos, 4, BLOCK_TIER_6);
		}

		return false;
	}

	/**
	 * Emerald blocks 4 blocks away from cauldron up, down, left, right
	 */
	public static boolean checkTier7(World world, BlockPos midPos) {
		if (checkTier6(world, midPos)) {
			return checkTierNCornersOnly(world, midPos, 7, BLOCK_TIER_7);
		}

		return false;
	}

	private static boolean checkTierNCornersOnly(World world, BlockPos midPos, int distance, Block tierBlock) {
		BlockPos up = new BlockPos(midPos.getX(), midPos.getY() - 1, midPos.getZ() + distance);
		BlockPos left = new BlockPos(midPos.getX() - distance, midPos.getY() - 1, midPos.getZ());
		BlockPos down = new BlockPos(midPos.getX(), midPos.getY() - 1, midPos.getZ() - distance);
		BlockPos right = new BlockPos(midPos.getX() + distance, midPos.getY() - 1, midPos.getZ());

		return Block.isEqualTo(world.getBlockState(up).getBlock(), tierBlock)
				&& Block.isEqualTo(world.getBlockState(left).getBlock(), tierBlock)
				&& Block.isEqualTo(world.getBlockState(down).getBlock(), tierBlock)
				&& Block.isEqualTo(world.getBlockState(right).getBlock(), tierBlock);
	}

	private static boolean checkTierNSidesOnly(World world, BlockPos midPos, int distance, Block tierBlock) {
		BlockPos rightUp = new BlockPos(midPos.getX() + distance, midPos.getY() - 1, midPos.getZ() + distance);
		BlockPos leftUp = new BlockPos(midPos.getX() - distance, midPos.getY() - 1, midPos.getZ() + distance);
		BlockPos leftDown = new BlockPos(midPos.getX() - distance, midPos.getY() - 1, midPos.getZ() - distance);
		BlockPos rightDown = new BlockPos(midPos.getX() + distance, midPos.getY() - 1, midPos.getZ() - distance);

		return Block.isEqualTo(world.getBlockState(rightUp).getBlock(), tierBlock)
				&& Block.isEqualTo(world.getBlockState(leftUp).getBlock(), tierBlock)
				&& Block.isEqualTo(world.getBlockState(leftDown).getBlock(), tierBlock)
				&& Block.isEqualTo(world.getBlockState(rightDown).getBlock(), tierBlock);
	}
}