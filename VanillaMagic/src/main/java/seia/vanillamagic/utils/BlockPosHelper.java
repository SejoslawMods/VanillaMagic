package seia.vanillamagic.utils;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
	
	public static void printCoords(BlockPos pos)
	{
		System.out.println(" X = " + pos.getX());
		System.out.println(" Y = " + pos.getY());
		System.out.println(" Z = " + pos.getZ());
	}
	
	public static void printCoords(Block block, BlockPos pos)
	{
		System.out.println(block.toString());
		printCoords(pos);
	}
	
	public static void printCoords(World world, BlockPos pos)
	{
		Block block = world.getBlockState(pos).getBlock();
		printCoords(block, pos);
	}
	
	public static void printCoords(EntityPlayer player, BlockPos pos)
	{
		printCoords(player.worldObj, pos);
	}
}