package com.github.sejoslaw.vanillamagic.common.tileentity.machine.farm;

import com.github.sejoslaw.vanillamagic.api.tileentity.machine.farm.IFarm;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class TreeHarvestUtils {
    private int horizontalRange;
    private int verticalRange;
    private BlockPos origin;

    private static boolean isLeaves(BlockState state) {
        return state.getMaterial() == Material.LEAVES;
    }

    public void harvest(IFarm farm, BlockPos pos, HarvestResult result) {
        this.horizontalRange = farm.getWorkRadius() + 7;
        this.verticalRange = 30;
        this.origin = new BlockPos(origin);

        World world = farm.asTileEntity().getWorld();
        BlockState woodState = world.getBlockState(pos);

        harvestUp(world, pos, result, new BaseHarvestTarget(woodState.getBlock()));
    }

    protected void harvestUp(World world, BlockPos pos, HarvestResult result, BaseHarvestTarget target) {
        if (!isInHarvestBounds(pos) || result.harvestedBlocks.contains(pos)) {
            return;
        }

        BlockState state = world.getBlockState(pos);
        boolean isLeaves = isLeaves(state);

        if (target.isTarget(state) || isLeaves) {
            result.harvestedBlocks.add(pos);

            for (Direction dir : Direction.values()) {
                if (dir != Direction.DOWN) {
                    harvestUp(world, pos.offset(dir), result, target);
                }
            }
        } else {
            harvestAdjacentWood(world, pos, result, target);

            for (Direction dir : Direction.Plane.HORIZONTAL) {
                BlockPos logPos = pos.offset(dir);
                BlockState logState = world.getBlockState(logPos);

                if (isLeaves(logState)) {
                    harvestAdjacentWood(world, logPos, result, target);
                }
            }
        }
    }

    private void harvestAdjacentWood(World world, BlockPos pos, HarvestResult result, BaseHarvestTarget target) {
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos logPos = pos.offset(dir);

            if (target.isTarget(world.getBlockState(logPos))) {
                harvestUp(world, logPos, result, target);
            }
        }
    }

    private boolean isInHarvestBounds(BlockPos pos) {
        return Math.abs(this.origin.getX() - pos.getX()) <= this.horizontalRange
                && Math.abs(this.origin.getZ() - pos.getZ()) <= this.horizontalRange
                && Math.abs(this.origin.getY() - pos.getY()) <= this.verticalRange;
    }

    private static class BaseHarvestTarget {
        private final Block wood;

        BaseHarvestTarget(Block wood) {
            this.wood = wood;
        }

        boolean isTarget(BlockState bs) {
            return bs.getBlock() == wood;
        }
    }
}