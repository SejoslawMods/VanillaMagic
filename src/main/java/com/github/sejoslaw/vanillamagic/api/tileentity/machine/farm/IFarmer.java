package com.github.sejoslaw.vanillamagic.api.tileentity.machine.farm;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface IFarmer {
    int getDefaultAge();

    int getMaxAge();

    int getAge(BlockState state);

    boolean prepareBlock(IFarm farm, BlockPos pos, Block block, BlockState state);

    boolean canHarvest(IFarm farm, BlockPos pos, Block block, BlockState state);

    boolean canPlant(ItemStack stack);

    IHarvestResult harvestBlock(IFarm farm, BlockPos pos, Block block, BlockState state);
}