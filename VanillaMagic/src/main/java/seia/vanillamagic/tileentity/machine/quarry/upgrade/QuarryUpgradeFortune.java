package seia.vanillamagic.tileentity.machine.quarry.upgrade;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class QuarryUpgradeFortune
{
	Random rand = new Random();
	public List<ItemStack> getDrops(Block blockToDig, IBlockAccess world, BlockPos workingPos, IBlockState workingPosState, int fortune) 
	{
		List<ItemStack> list = new ArrayList<ItemStack>();
		Item item = blockToDig.getItemDropped(workingPosState, rand, fortune);
		list.add(new ItemStack(item, blockToDig.quantityDropped(rand) * fortune, blockToDig.damageDropped(workingPosState)));
		return list;
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
	}
}