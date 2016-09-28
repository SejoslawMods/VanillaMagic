package seia.vanillamagic.machine.quarry.upgrade;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class QuarryUpgradeFortune
{
	public List<ItemStack> getDrops(Block blockToDig, IBlockAccess world, BlockPos workingPos, IBlockState workingPosState, int fortune) 
	{
		return blockToDig.getDrops(world, workingPos, workingPosState, fortune);
	}
	
	public static class One extends QuarryUpgradeFortune implements IQuarryUpgrade
	{
		public String getUpgradeName()
		{
			return "Fortune 1";
		}
		
		public Block getBlock() 
		{
			return Blocks.LAPIS_BLOCK;
		}
		
		public List<ItemStack> getDrops(Block blockToDig, IBlockAccess world, BlockPos workingPos, IBlockState workingPosState) 
		{
			return super.getDrops(blockToDig, world, workingPos, workingPosState, 1);
		}
	}
	
	public static class Two extends QuarryUpgradeFortune implements IQuarryUpgrade
	{
		public String getUpgradeName()
		{
			return "Fortune 2";
		}
		
		public Block getBlock() 
		{
			return Blocks.IRON_BLOCK;
		}
		
		public List<ItemStack> getDrops(Block blockToDig, IBlockAccess world, BlockPos workingPos, IBlockState workingPosState) 
		{
			return super.getDrops(blockToDig, world, workingPos, workingPosState, 2);
		}
		
		public Class<? extends IQuarryUpgrade> requiredUpgrade()
		{
			return One.class;
		}
	}
	
	public static class Three extends QuarryUpgradeFortune implements IQuarryUpgrade
	{
		public String getUpgradeName()
		{
			return "Fortune 3";
		}
		
		public Block getBlock() 
		{
			return Blocks.GOLD_BLOCK;
		}
		
		public List<ItemStack> getDrops(Block blockToDig, IBlockAccess world, BlockPos workingPos, IBlockState workingPosState) 
		{
			return super.getDrops(blockToDig, world, workingPos, workingPosState, 3);
		}
		
		public Class<? extends IQuarryUpgrade> requiredUpgrade()
		{
			return Two.class;
		}
	}
}