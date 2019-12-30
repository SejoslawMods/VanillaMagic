package com.github.sejoslaw.vanillamagic.common.tileentity.machine.farm.farmer;

import com.github.sejoslaw.vanillamagic.api.tileentity.machine.farm.IFarm;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CocoaBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Comparator;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class FarmerCocoa extends FarmerCustomSeed {
    public FarmerCocoa() {
        super(Blocks.COCOA, new ItemStack(Items.COCOA_BEANS, 1));

        this.requiresFarmland = false;
    }

    public int getMaxAge() {
        return CocoaBlock.AGE.getAllowedValues().stream().max(Comparator.naturalOrder()).get();
    }

    public int getAge(BlockState state) {
        return state.get(CocoaBlock.AGE);
    }

    public boolean canHarvest(IFarm farm, BlockPos pos, Block block, BlockState state) {
        return (block == getPlantedBlock() || block instanceof CocoaBlock) && getAge(state) == getMaxAge();
    }

    protected boolean canPlant(IFarm farm, World world, BlockPos pos) {
        return getPlantDirection(world, pos) != null;
    }

    private Direction getPlantDirection(World world, BlockPos pos) {
        if (!world.isAirBlock(pos)) {
            return null;
        }

        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos p = pos.offset(dir);

            if (validBlock(world.getBlockState(p))) {
                return dir;
            }
        }
        return null;
    }

    private boolean validBlock(BlockState state) {
        return state.getBlock() == Blocks.JUNGLE_LOG;
    }
}