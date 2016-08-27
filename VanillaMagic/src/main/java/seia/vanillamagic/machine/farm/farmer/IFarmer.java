package seia.vanillamagic.machine.farm.farmer;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import seia.vanillamagic.machine.farm.IHarvestResult;
import seia.vanillamagic.machine.farm.TileFarm;

public interface IFarmer 
{
	boolean prepareBlock(TileFarm farm, BlockPos pos, Block block, IBlockState state);
	
	boolean canHarvest(TileFarm farm, BlockPos pos, Block block, IBlockState state);

	boolean canPlant(ItemStack stack);

	@Nullable
	IHarvestResult harvestBlock(TileFarm farm, BlockPos pos, Block block, IBlockState state);
}