package seia.vanillamagic.util;

import net.minecraft.block.BlockFurnace;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OreMultiplierChecker 
{
	private OreMultiplierChecker()
	{
	}
	
	public static boolean check(World world, BlockPos cauldronPos)
	{
		BlockPos furnaceNorth = cauldronPos.offset(EnumFacing.NORTH);
		BlockPos furnaceSouth = cauldronPos.offset(EnumFacing.SOUTH);
		BlockPos furnaceEast = cauldronPos.offset(EnumFacing.EAST);
		BlockPos furnaceWest = cauldronPos.offset(EnumFacing.WEST);
		
		if(world.getBlockState(furnaceNorth).getBlock() instanceof BlockFurnace)
		{
			if(world.getBlockState(furnaceSouth).getBlock() instanceof BlockFurnace)
			{
				if(world.getBlockState(furnaceEast).getBlock() instanceof BlockFurnace)
				{
					if(world.getBlockState(furnaceWest).getBlock() instanceof BlockFurnace)
					{
						return true;
					}
				}
			}
		}
		return false;
	}
}