package com.github.sejoslaw.vanillamagic.common.tileentity.machine.farm.farmer;

import com.github.sejoslaw.vanillamagic.api.tileentity.machine.farm.IFarm;
import com.github.sejoslaw.vanillamagic.api.tileentity.machine.farm.IHarvestResult;
import com.github.sejoslaw.vanillamagic.common.tileentity.machine.farm.HarvestResult;
import com.github.sejoslaw.vanillamagic.common.util.BlockUtil;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.StemBlock;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class FarmerStem extends FarmerCustomSeed {
    private static final HeightComparator COMP = new HeightComparator();

    public FarmerStem(Block plantedBlock, ItemStack seeds) {
        super(plantedBlock, seeds);
    }

    public int getMaxAge() {
        return StemBlock.AGE.getAllowedValues().stream().max(Integer::compare).get();
    }

    public int getAge(BlockState state) {
        return state.get(StemBlock.AGE);
    }

    public boolean prepareBlock(IFarm farm, BlockPos pos, Block block, BlockState state) {
        if (plantedBlock == block) {
            return true;
        }

        return plantFromInventory(farm, pos);
    }

    public boolean canHarvest(IFarm farm, BlockPos pos, Block block, BlockState state) {
        BlockPos up = pos.offset(Direction.UP);
        Block upBlock = farm.asTileEntity().getWorld().getBlockState(up).getBlock();

        return BlockUtil.areEqual(upBlock, plantedBlock);
    }

    public boolean canPlant(ItemStack stack) {
        return seeds.isItemEqual(stack);
    }

    public IHarvestResult harvestBlock(IFarm farm, BlockPos pos, Block block, BlockState state) {
        World world = farm.asTileEntity().getWorld();
        HarvestResult result = new HarvestResult();
        BlockPos harvestCoord = pos;
        boolean done = false;
        Block harvestBlock;

        do {
            harvestCoord = harvestCoord.offset(Direction.UP);
            boolean hasHoe = farm.hasHoe();
            harvestBlock = world.getBlockState(harvestCoord).getBlock();

            if (plantedBlock == harvestBlock && hasHoe) {
                result.harvestedBlocks.add(harvestCoord);

                NonNullList<ItemStack> drops = NonNullList.create();
                drops.addAll(Block.getDrops(state, (ServerWorld) world, pos, farm.asTileEntity()));

                for (ItemStack drop : drops) {
                    result.drops.add(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop.copy()));
                }

                farm.damageHoe(1, new BlockPos(harvestCoord));
            } else {
                done = true;
            }
        } while (!done);

        List<BlockPos> toClear = new ArrayList<>(result.getHarvestedBlocks());
        Collections.sort(toClear, COMP);

        for (BlockPos coord : toClear) {
            world.setBlockState(coord, Blocks.AIR.getDefaultState());
        }

        return result;
    }

    protected boolean plantFromInventory(IFarm farm, BlockPos pos) {
        World world = farm.asTileEntity().getWorld();
        ItemStack seedStack = farm.takeSeedFromSupplies(seeds, pos);

        if (canPlant(farm, world, pos) && !ItemStackUtil.isNullStack(seedStack)) {
            return plant(farm, world, pos, seedStack);
        }

        return false;
    }

    private static class HeightComparator implements Comparator<BlockPos> {
        public int compare(BlockPos pos1, BlockPos pos2) {
            return -compare(pos1.getY(), pos2.getY());
        }

        public static int compare(int x, int y) {
            return Integer.compare(x, y);
        }
    }
}