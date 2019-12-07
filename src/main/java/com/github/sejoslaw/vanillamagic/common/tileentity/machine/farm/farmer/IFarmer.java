package com.github.sejoslaw.vanillamagic.tileentity.machine.farm.farmer;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import com.github.sejoslaw.vanillamagic.tileentity.machine.farm.IHarvestResult;
import com.github.sejoslaw.vanillamagic.tileentity.machine.farm.TileFarm;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface IFarmer {
	boolean prepareBlock(TileFarm farm, BlockPos pos, Block block, IBlockState state);

	boolean canHarvest(TileFarm farm, BlockPos pos, Block block, IBlockState state);

	boolean canPlant(ItemStack stack);

	@Nullable
	IHarvestResult harvestBlock(TileFarm farm, BlockPos pos, Block block, IBlockState state);
}