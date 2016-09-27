package seia.vanillamagic.machine.quarry.upgrade;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class QuarryUpgradeFortune
{
	public static class One extends QuarryUpgradeFortune implements IQuarryUpgrade
	{
		public Block getBlock() 
		{
		}
		
		public List<ItemStack> getDrops(Block blockToDig, IBlockAccess world, BlockPos workingPos, IBlockState workingPosState) 
		{
			return blockToDig.getDrops(world, workingPos, workingPosState, 1);
		}
	}
	
	public static class Two extends QuarryUpgradeFortune implements IQuarryUpgrade
	{
		public Block getBlock() 
		{
		}
		
		public List<ItemStack> getDrops(Block blockToDig, IBlockAccess world, BlockPos workingPos, IBlockState workingPosState) 
		{
			return blockToDig.getDrops(world, workingPos, workingPosState, 2);
		}
		
		public Class<? extends IQuarryUpgrade> requiredUpgrade()
		{
			return One.class;
		}
	}
	
	public static class Three extends QuarryUpgradeFortune implements IQuarryUpgrade
	{
		public Block getBlock() 
		{
		}
		
		public List<ItemStack> getDrops(Block blockToDig, IBlockAccess world, BlockPos workingPos, IBlockState workingPosState) 
		{
			return blockToDig.getDrops(world, workingPos, workingPosState, 3);
		}
		
		public Class<? extends IQuarryUpgrade> requiredUpgrade()
		{
			return Two.class;
		}
	}
}