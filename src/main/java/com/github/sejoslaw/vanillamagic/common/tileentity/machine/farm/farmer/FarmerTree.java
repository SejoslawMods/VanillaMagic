package com.github.sejoslaw.vanillamagic.common.tileentity.machine.farm.farmer;

import com.github.sejoslaw.vanillamagic.api.tileentity.machine.farm.IFarm;
import com.github.sejoslaw.vanillamagic.api.tileentity.machine.farm.IFarmer;
import com.github.sejoslaw.vanillamagic.api.tileentity.machine.farm.IHarvestResult;
import com.github.sejoslaw.vanillamagic.common.tileentity.machine.farm.HarvestResult;
import com.github.sejoslaw.vanillamagic.common.tileentity.machine.farm.TreeHarvestUtils;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.IShearable;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class FarmerTree implements IFarmer {
    private static final HeightComparator COMP = new HeightComparator();

    protected Block saplingBlock;
    protected ItemStack saplingStack;
    protected Block[] woodBlocks;
    protected TreeHarvestUtils harvester = new TreeHarvestUtils();

    public FarmerTree(ItemStack saplingBlock, ItemStack wood) {
        this(Block.getBlockFromItem(saplingBlock.getItem()), Block.getBlockFromItem(wood.getItem()));
    }

    public FarmerTree(Block saplingBlock, Block... wood) {
        this.saplingBlock = saplingBlock;
        this.saplingStack = new ItemStack(saplingBlock);
        this.woodBlocks = wood;
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

    public boolean canHarvest(IFarm farm, BlockPos pos, Block block, BlockState state) {
        return isWood(block);
    }

    protected boolean isWood(Block block) {
        for (Block wood : woodBlocks) {
            if (block == wood) {
                return true;
            }
        }

        return false;
    }

    public boolean canPlant(ItemStack stack) {
        return !ItemStackUtil.isNullStack(stack) && stack.getItem() == saplingStack.getItem();
    }

    public boolean prepareBlock(IFarm farm, BlockPos pos, Block block, BlockState state) {
        if (block == saplingBlock) {
            return true;
        }

        return plantFromInventory(farm, pos, block, state);
    }

    protected boolean plantFromInventory(IFarm farm, BlockPos pos, Block block, BlockState state) {
        World world = farm.asTileEntity().getWorld();

        if (canPlant(world, pos)) {
            ItemStack seed = farm.takeSeedFromSupplies(saplingStack, pos);

            if (!ItemStackUtil.isNullStack(seed)) {
                return plant(farm, world, pos, seed);
            }
        }

        return false;
    }

    protected boolean canPlant(World world, BlockPos pos) {
        BlockPos groundPos = pos.down();
        BlockState groundState = world.getBlockState(groundPos);
        Block groundBlock = groundState.getBlock();
        IPlantable plantable = (IPlantable) saplingBlock;

        return groundBlock.canSustainPlant(groundState, world, groundPos, Direction.UP, plantable);
    }

    protected boolean plant(IFarm farm, World world, BlockPos pos, ItemStack seedStack) {
        if (canPlant(world, pos)) {
            return world.setBlockState(pos, saplingBlock.getDefaultState(), 1 | 2);
        }

        return false;
    }

    public IHarvestResult harvestBlock(IFarm farm, BlockPos pos, Block block, BlockState state) {
        boolean hasAxe = farm.hasAxe();

        if (!hasAxe) {
            return null;
        }

        World world = farm.asTileEntity().getWorld();
        final int fortune = farm.getMaxLootingValue();
        HarvestResult res = new HarvestResult();

        harvester.harvest(farm, this, pos, res);
        Collections.sort(res.harvestedBlocks, COMP);

        List<BlockPos> actualHarvests = new ArrayList<>();
        boolean hasShears = farm.hasShears();
        int noShearingPercentage = farm.isLowOnSaplings(pos);
        int shearCount = 0;

        for (int i = 0; i < res.harvestedBlocks.size() && hasAxe; ++i) {
            BlockPos harvestedBlockPos = res.harvestedBlocks.get(i);
            BlockState harvestedBlockState = world.getBlockState(harvestedBlockPos);
            Block harvestedBlock = harvestedBlockState.getBlock();
            NonNullList<ItemStack> drops;

            boolean wasSheared = false;
            boolean wasAxed = false;
            float chance = 1.0F;

            if (harvestedBlock instanceof IShearable && hasShears && ((shearCount / res.harvestedBlocks.size() + noShearingPercentage) < 100)) {
                drops = (NonNullList<ItemStack>) ((IShearable) harvestedBlock).onSheared(null, world, harvestedBlockPos, 0);
                wasSheared = true;
                shearCount += 100;
            } else {
                drops = (NonNullList<ItemStack>) Block.getDrops(state, (ServerWorld) world, pos, farm.asTileEntity());
                chance = ForgeEventFactory.fireBlockHarvesting(drops, world, harvestedBlockPos, harvestedBlockState, fortune, chance, false, null/* fakePlayer */);
                wasAxed = true;
            }

            for (ItemStack drop : drops) {
                if (world.rand.nextFloat() <= chance) {
                    res.drops.add(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop.copy()));
                }
            }

            if (wasAxed) {
                farm.damageAxe(harvestedBlock, new BlockPos(harvestedBlockPos));
                hasAxe = farm.hasAxe();
            } else if (wasSheared) {
                farm.damageShears(harvestedBlock, new BlockPos(harvestedBlockPos));
                hasShears = farm.hasShears();
            }

            world.setBlockState(harvestedBlockPos, Blocks.AIR.getDefaultState());
            actualHarvests.add(harvestedBlockPos);
        }

        res.harvestedBlocks.clear();
        res.harvestedBlocks.addAll(actualHarvests);

        for (ItemEntity drop : res.drops) {
            if (canPlant(drop.getItem()) && plant(farm, world, pos, drop.getItem())) {
                res.drops.remove(drop);
                break;
            }
        }

        return res;
    }

    private static class HeightComparator implements Comparator<BlockPos> {
        public int compare(BlockPos o1, BlockPos o2) {
            return compare(o2.getY(), o1.getY());
        }

        public static int compare(int x, int y) {
            return Integer.compare(x, y);
        }
    }
}