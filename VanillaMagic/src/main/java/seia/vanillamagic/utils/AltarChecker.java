package seia.vanillamagic.utils;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AltarChecker 
{
	public static final Block BLOCK_TIER_1 = Blocks.REDSTONE_WIRE;
	public static final Block BLOCK_TIER_2 = Blocks.IRON_BLOCK;
	public static final Block BLOCK_TIER_3 = Blocks.GOLD_BLOCK;
	public static final Block BLOCK_TIER_4 = Blocks.REDSTONE_BLOCK;
	public static final Block BLOCK_TIER_5 = Blocks.LAPIS_BLOCK;
	public static final Block BLOCK_TIER_6 = Blocks.DIAMOND_BLOCK;
	public static final Block BLOCK_TIER_7 = Blocks.EMERALD_BLOCK;
	
	public static Block getBlockByTier(int tier)
	{
		if(tier == 1)
		{
			return BLOCK_TIER_1;
		}
		else if(tier == 2)
		{
			return BLOCK_TIER_2;
		}
		else if(tier == 3)
		{
			return BLOCK_TIER_3;
		}
		else if(tier == 4)
		{
			return BLOCK_TIER_4;
		}
		else if(tier == 5)
		{
			return BLOCK_TIER_5;
		}
		else if(tier == 6)
		{
			return BLOCK_TIER_6;
		}
		else if(tier == 7)
		{
			return BLOCK_TIER_7;
		}
		return null;
	}
	
	public static boolean checkAltarTier(World world, BlockPos midPos, int tier)
	{
		if(tier == 1)
		{
			return checkTier1(world, midPos);
		}
		else if(tier == 2)
		{
			return checkTier2(world, midPos);
		}
		else if(tier == 3)
		{
			return checkTier3(world, midPos);
		}
		else if(tier == 4)
		{
			return checkTier4(world, midPos);
		}
		else if(tier == 5)
		{
			return checkTier5(world, midPos);
		}
		else if(tier == 6)
		{
			return checkTier6(world, midPos);
		}
		else if(tier == 7)
		{
			return checkTier7(world, midPos);
		}
		return false;
	}
	
	/*
	 * Cauldron + redstone around
	 */
	public static boolean checkTier1(World world, BlockPos midPos)
	{
		BlockPos right = new BlockPos(midPos.getX() + 1, midPos.getY(), midPos.getZ());
		BlockPos rightUp = new BlockPos(midPos.getX() + 1, midPos.getY(), midPos.getZ() + 1);
		BlockPos up = new BlockPos(midPos.getX(), midPos.getY(), midPos.getZ() + 1);
		BlockPos leftUp = new BlockPos(midPos.getX() - 1, midPos.getY(), midPos.getZ() + 1);
		BlockPos left = new BlockPos(midPos.getX() - 1, midPos.getY(), midPos.getZ());
		BlockPos leftDown = new BlockPos(midPos.getX() - 1, midPos.getY(), midPos.getZ() - 1);
		BlockPos down = new BlockPos(midPos.getX(), midPos.getY(), midPos.getZ() - 1);
		BlockPos rightDown = new BlockPos(midPos.getX() + 1, midPos.getY(), midPos.getZ() - 1);
		
		if(Block.isEqualTo(world.getBlockState(right).getBlock(), BLOCK_TIER_1))
		{
			if(Block.isEqualTo(world.getBlockState(right).getBlock(), BLOCK_TIER_1))
			{
				if(Block.isEqualTo(world.getBlockState(right).getBlock(), BLOCK_TIER_1))
				{
					if(Block.isEqualTo(world.getBlockState(right).getBlock(), BLOCK_TIER_1))
					{
						if(Block.isEqualTo(world.getBlockState(right).getBlock(), BLOCK_TIER_1))
						{
							if(Block.isEqualTo(world.getBlockState(right).getBlock(), BLOCK_TIER_1))
							{
								if(Block.isEqualTo(world.getBlockState(right).getBlock(), BLOCK_TIER_1))
								{
									if(Block.isEqualTo(world.getBlockState(right).getBlock(), BLOCK_TIER_1))
									{
										return true;
									}
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	/*
	 * Iron on corners
	 */
	public static boolean checkTier2(World world, BlockPos midPos)
	{
		if(checkTier1(world, midPos))
		{
			return checkTier2Only(world, midPos);
		}
		return false;
	}

	public static boolean checkTier2Only(World world, BlockPos midPos)
	{
		BlockPos rightUp = new BlockPos(midPos.getX() + 2, midPos.getY() - 1, midPos.getZ() + 2);
		BlockPos leftUp = new BlockPos(midPos.getX() - 2, midPos.getY() - 1, midPos.getZ() + 2);
		BlockPos leftDown = new BlockPos(midPos.getX() - 2, midPos.getY() - 1, midPos.getZ() - 2);
		BlockPos rightDown = new BlockPos(midPos.getX() + 2, midPos.getY() - 1, midPos.getZ() - 2);
		
		if(Block.isEqualTo(world.getBlockState(rightUp).getBlock(), BLOCK_TIER_2))
		{
			if(Block.isEqualTo(world.getBlockState(leftUp).getBlock(), BLOCK_TIER_2))
			{
				if(Block.isEqualTo(world.getBlockState(leftDown).getBlock(), BLOCK_TIER_2))
				{
					if(Block.isEqualTo(world.getBlockState(rightDown).getBlock(), BLOCK_TIER_2))
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	/*
	 * Gold blocks 2 blocks away from cauldron up, down, left, right
	 */
	public static boolean checkTier3(World world, BlockPos midPos)
	{
		if(checkTier2(world, midPos))
		{
			return checkTier3Only(world, midPos);
		}
		return false;
	}
	
	public static boolean checkTier3Only(World world, BlockPos midPos)
	{
		BlockPos up = new BlockPos(midPos.getX(), midPos.getY() - 1, midPos.getZ() + 3);
		BlockPos left = new BlockPos(midPos.getX() - 3, midPos.getY() - 1, midPos.getZ());
		BlockPos down = new BlockPos(midPos.getX(), midPos.getY() - 1, midPos.getZ() - 3);
		BlockPos right = new BlockPos(midPos.getX() + 3, midPos.getY() - 1, midPos.getZ());
		
		if(Block.isEqualTo(world.getBlockState(up).getBlock(), BLOCK_TIER_3))
		{
			if(Block.isEqualTo(world.getBlockState(left).getBlock(), BLOCK_TIER_3))
			{
				if(Block.isEqualTo(world.getBlockState(down).getBlock(), BLOCK_TIER_3))
				{
					if(Block.isEqualTo(world.getBlockState(right).getBlock(), BLOCK_TIER_3))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/*
	 * Redstone on corners after iron blocks
	 */
	public static boolean checkTier4(World world, BlockPos midPos)
	{
		if(checkTier3(world, midPos))
		{
			return checkTier4Only(world, midPos);
		}
		return false;
	}
	
	public static boolean checkTier4Only(World world, BlockPos midPos)
	{
		BlockPos rightUp = new BlockPos(midPos.getX() + 3, midPos.getY() - 1, midPos.getZ() + 3);
		BlockPos leftUp = new BlockPos(midPos.getX() - 3, midPos.getY() - 1, midPos.getZ() + 3);
		BlockPos leftDown = new BlockPos(midPos.getX() - 3, midPos.getY() - 1, midPos.getZ() - 3);
		BlockPos rightDown = new BlockPos(midPos.getX() + 3, midPos.getY() - 1, midPos.getZ() - 3);
		
		if(Block.isEqualTo(world.getBlockState(rightUp).getBlock(), BLOCK_TIER_4))
		{
			if(Block.isEqualTo(world.getBlockState(leftUp).getBlock(), BLOCK_TIER_4))
			{
				if(Block.isEqualTo(world.getBlockState(leftDown).getBlock(), BLOCK_TIER_4))
				{
					if(Block.isEqualTo(world.getBlockState(rightDown).getBlock(), BLOCK_TIER_4))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/*
	 * Lapis blocks 4 blocks away from cauldron up, down, left, right
	 */
	public static boolean checkTier5(World world, BlockPos midPos)
	{
		if(checkTier4(world, midPos))
		{
			return checkTier5Only(world, midPos);
		}
		return false;
	}
	
	public static boolean checkTier5Only(World world, BlockPos midPos)
	{
		BlockPos up = new BlockPos(midPos.getX(), midPos.getY() - 1, midPos.getZ() + 5);
		BlockPos left = new BlockPos(midPos.getX() - 5, midPos.getY() - 1, midPos.getZ());
		BlockPos down = new BlockPos(midPos.getX(), midPos.getY() - 1, midPos.getZ() - 5);
		BlockPos right = new BlockPos(midPos.getX() + 5, midPos.getY() - 1, midPos.getZ());
		
		if(Block.isEqualTo(world.getBlockState(up).getBlock(), BLOCK_TIER_5))
		{
			if(Block.isEqualTo(world.getBlockState(left).getBlock(), BLOCK_TIER_5))
			{
				if(Block.isEqualTo(world.getBlockState(down).getBlock(), BLOCK_TIER_5))
				{
					if(Block.isEqualTo(world.getBlockState(right).getBlock(), BLOCK_TIER_5))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/*
	 * Diamond blocks on corners after redstone blocks
	 */
	public static boolean checkTier6(World world, BlockPos midPos)
	{
		if(checkTier5(world, midPos))
		{
			return checkTier6Only(world, midPos);
		}
		return false;
	}
	
	public static boolean checkTier6Only(World world, BlockPos midPos)
	{
		BlockPos rightUp = new BlockPos(midPos.getX() + 4, midPos.getY() - 1, midPos.getZ() + 4);
		BlockPos leftUp = new BlockPos(midPos.getX() - 4, midPos.getY() - 1, midPos.getZ() + 4);
		BlockPos leftDown = new BlockPos(midPos.getX() - 4, midPos.getY() - 1, midPos.getZ() - 4);
		BlockPos rightDown = new BlockPos(midPos.getX() + 4, midPos.getY() - 1, midPos.getZ() - 4);
		
		if(Block.isEqualTo(world.getBlockState(rightUp).getBlock(), BLOCK_TIER_6))
		{
			if(Block.isEqualTo(world.getBlockState(leftUp).getBlock(), BLOCK_TIER_6))
			{
				if(Block.isEqualTo(world.getBlockState(leftDown).getBlock(), BLOCK_TIER_6))
				{
					if(Block.isEqualTo(world.getBlockState(rightDown).getBlock(), BLOCK_TIER_6))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/*
	 * Emerald blocks 4 blocks away from cauldron up, down, left, right
	 */
	public static boolean checkTier7(World world, BlockPos midPos)
	{
		if(checkTier6(world, midPos))
		{
			return checkTier7Only(world, midPos);
		}
		return false;
	}
	
	public static boolean checkTier7Only(World world, BlockPos midPos)
	{
		BlockPos up = new BlockPos(midPos.getX(), midPos.getY() - 1, midPos.getZ() + 7);
		BlockPos left = new BlockPos(midPos.getX() - 7, midPos.getY() - 1, midPos.getZ());
		BlockPos down = new BlockPos(midPos.getX(), midPos.getY() - 1, midPos.getZ() - 7);
		BlockPos right = new BlockPos(midPos.getX() + 7, midPos.getY() - 1, midPos.getZ());
		
		if(Block.isEqualTo(world.getBlockState(up).getBlock(), BLOCK_TIER_7))
		{
			if(Block.isEqualTo(world.getBlockState(left).getBlock(), BLOCK_TIER_7))
			{
				if(Block.isEqualTo(world.getBlockState(down).getBlock(), BLOCK_TIER_7))
				{
					if(Block.isEqualTo(world.getBlockState(right).getBlock(), BLOCK_TIER_7))
					{
						return true;
					}
				}
			}
		}
		return false;
	}
}