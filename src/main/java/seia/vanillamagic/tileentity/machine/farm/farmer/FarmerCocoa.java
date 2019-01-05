package seia.vanillamagic.tileentity.machine.farm.farmer;

import static net.minecraft.block.BlockHorizontal.FACING;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.tileentity.machine.farm.TileFarm;

public class FarmerCocoa extends FarmerCustomSeed 
{
	public FarmerCocoa()
	{
		super(Blocks.COCOA, new ItemStack(Items.DYE, 1, 3));
		this.requiresFarmland = false;
	}
	  
	public boolean canHarvest(TileFarm farm, BlockPos pos, Block block, IBlockState state) 
	{
		if (block == getPlantedBlock() && state.getValue(BlockCocoa.AGE) == 2) return true;
		else if(block instanceof BlockCocoa && state.getValue(BlockCocoa.AGE) == 2) return true;
		return false;
	}
	  
	protected boolean plant(TileFarm farm, World worldObj, BlockPos pos) 
	{
		EnumFacing dir = getPlantDirection(worldObj, pos);
		if (dir == null) return false;
		
		IBlockState iBlockState = getPlantedBlock().getDefaultState().withProperty(FACING, dir);
		if (worldObj.setBlockState(pos, iBlockState, 1 | 2)) return true;
		
		return false;
	}
	  
	protected boolean canPlant(TileFarm farm, World worldObj, BlockPos pos) 
	{
		return getPlantDirection(worldObj, pos) != null;
	}

	@Nullable
	private EnumFacing getPlantDirection(World worldObj, BlockPos pos) 
	{
		if (!worldObj.isAirBlock(pos)) return null;
		
		for (EnumFacing dir : EnumFacing.HORIZONTALS) 
		{
			BlockPos p = pos.offset(dir);
			if (validBlock(worldObj.getBlockState(p))) return dir;
		}
		return null;
	}

	private boolean validBlock(IBlockState state) 
	{
		return state.getBlock() == Blocks.LOG && state.getValue(BlockOldLog.VARIANT) == BlockPlanks.EnumType.JUNGLE;
	}
}