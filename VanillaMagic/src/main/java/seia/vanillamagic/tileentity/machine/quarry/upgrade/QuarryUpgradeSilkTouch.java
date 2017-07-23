package seia.vanillamagic.tileentity.machine.quarry.upgrade;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import seia.vanillamagic.api.tileentity.machine.IQuarryUpgrade;

public class QuarryUpgradeSilkTouch implements IQuarryUpgrade
{
	public String getUpgradeName()
	{
		return "Silk-Touch";
	}
	
	public Block getBlock() 
	{
		return Blocks.QUARTZ_BLOCK;
	}
	
	public List<ItemStack> getDrops(Block blockToDig, IBlockAccess world, BlockPos workingPos, IBlockState workingPosState)
	{
		List<ItemStack> list = new ArrayList<ItemStack>();
		// Silk-Touch returns the digged block.
		list.add(new ItemStack(blockToDig));
		return list;
	}
}