package com.github.sejoslaw.vanillamagic.common.tileentity.machine.farm.farmer;

import com.github.sejoslaw.vanillamagic.api.tileentity.machine.farm.IFarm;
import com.github.sejoslaw.vanillamagic.api.tileentity.machine.farm.IFarmer;
import com.github.sejoslaw.vanillamagic.api.tileentity.machine.farm.IHarvestResult;
import com.github.sejoslaw.vanillamagic.common.tileentity.machine.farm.HarvestResult;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class FarmerFlowerPicker implements IFarmer {
    protected final List<ItemStack> flowers;

    public FarmerFlowerPicker(List<ItemStack> flowers) {
        this.flowers = flowers;
    }

    public FarmerFlowerPicker add(ItemStack flowers) {
        this.flowers.add(flowers);
        return this;
    }

    public int getDefaultAge() {
        return 0;
    }

    public int getMaxAge() {
        return 0;
    }

    public int getAge(BlockState state) {
        return 0;
    }

    public boolean prepareBlock(IFarm farm, BlockPos pos, Block block, BlockState state) {
        return false;
    }

    public boolean canHarvest(IFarm farm, BlockPos pos, Block block, BlockState state) {
        return block instanceof FlowerBlock;
    }

    public boolean canPlant(ItemStack stack) {
        return false;
    }

    public IHarvestResult harvestBlock(IFarm farm, BlockPos pos, Block block, BlockState state) {
        World world = farm.asTileEntity().getWorld();
        NonNullList<ItemStack> drops = NonNullList.create();

        if (canHarvest(farm, pos, block, state)) {
            drops.add(new ItemStack(block));
            farm.damageShears(block, pos);
        } else {
            if (!farm.hasHoe()) {
                return null;
            }

            Block.getDrops(state, (ServerWorld) world, pos, farm.asTileEntity());
            farm.damageHoe(1, pos);
        }

        List<ItemEntity> result = new ArrayList<>();

        for (ItemStack stack : drops) {
            result.add(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack.copy()));
        }

        world.setBlockState(pos, Blocks.AIR.getDefaultState());

        return new HarvestResult(result, pos);
    }
}