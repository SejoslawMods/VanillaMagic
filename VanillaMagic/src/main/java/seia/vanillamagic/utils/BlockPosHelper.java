package seia.vanillamagic.utils;

import net.minecraft.util.math.BlockPos;

public class BlockPosHelper
{
	public static boolean isSameBlockPos(BlockPos pos1, BlockPos pos2)
	{
		if(pos1.getX() == pos2.getX())
		{
			if(pos1.getY() == pos2.getY())
			{
				if(pos1.getZ() == pos2.getZ())
				{
					return true;
				}
			}
		}
		return false;
	}
}