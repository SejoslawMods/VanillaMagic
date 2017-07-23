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
import seia.vanillamagic.util.ItemStackUtil;
import seia.vanillamagic.util.SmeltingUtil;

public class QuarryUpgradeAutosmeltDigged implements IQuarryUpgrade
{
	public String getUpgradeName() 
	{
		return "Autosmelt";
	}
	
	public Block getBlock() 
	{
		return Blocks.FURNACE;
	}
	
	public List<ItemStack> getDrops(Block blockToDig, IBlockAccess world, BlockPos workingPos, IBlockState workingPosState)
	{
		List<ItemStack> list = new ArrayList<ItemStack>();
		ItemStack burnedStack = SmeltingUtil.getSmeltingResultAsNewStack(new ItemStack(blockToDig));
		if (!ItemStackUtil.isNullStack(burnedStack)) list.add(burnedStack);
		return list;
	}
}