package com.github.sejoslaw.vanillamagic.common.tileentity.machine.farm.farmer;

import com.github.sejoslaw.vanillamagic.api.tileentity.machine.farm.IFarm;
import com.github.sejoslaw.vanillamagic.api.tileentity.machine.farm.IFarmer;
import com.github.sejoslaw.vanillamagic.api.tileentity.machine.farm.IHarvestResult;
import com.github.sejoslaw.vanillamagic.common.tileentity.machine.farm.HarvestResult;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class FarmerCustomSeed implements IFarmer {
    protected final Block plantedBlock;
    protected final ItemStack seeds;

    public boolean requiresFarmland = true;
    public List<Block> tilledBlocks = new ArrayList<>();
    public boolean ignoreSustainCheck = false;
    public boolean checkGroundForFarmland = false;

    public FarmerCustomSeed(Block plantedBlock, ItemStack seeds) {
        this.plantedBlock = plantedBlock;
        this.seeds = seeds;

        addTilledBlock(Blocks.FARMLAND);
    }

    public void addTilledBlock(Block block) {
        tilledBlocks.add(block);
    }

    public int getDefaultAge() {
        return 0;
    }

    public Block getPlantedBlock() {
        return plantedBlock;
    }

    public ItemStack getSeeds() {
        return seeds;
    }

    public boolean canHarvest(IFarm farm, BlockPos pos, Block block, BlockState state) {
        return block == getPlantedBlock() && this.getDefaultAge() == this.getAge(state);
    }

    public boolean canPlant(ItemStack stack) {
        return !ItemStackUtil.isNullStack(stack) && stack.isItemEqual(getSeeds());
    }

    public boolean prepareBlock(IFarm farm, BlockPos pos, Block block, BlockState meta) {
        if (!farm.isAir(pos)) {
            return false;
        }

        if (requiresFarmland()) {
            if (isGroundTilled(farm, pos)) {
                return plantFromInventory(farm, pos);
            }

            if (farm.hasSeed(getSeeds())) {
                boolean tilled = tillBlock(farm, pos);

                if (!tilled) {
                    return false;
                }
            }
        }

        return plantFromInventory(farm, pos);
    }

    public boolean requiresFarmland() {
        return requiresFarmland;
    }

    protected boolean plantFromInventory(IFarm farm, BlockPos pos) {
        World world = farm.asTileEntity().getWorld();

        if (world == null) {
            return false;
        }

        ItemStack seed = farm.takeSeedFromSupplies(getSeeds(), pos);

        if (canPlant(farm, world, pos) && !ItemStackUtil.isNullStack(seed)) {
            return plant(farm, world, pos, seed);
        }

        return false;
    }

    public IHarvestResult harvestBlock(IFarm farm, BlockPos pos, Block block, BlockState state) {
        if (!canHarvest(farm, pos, block, state) || !farm.hasHoe()) {
            return null;
        }

        World world = farm.asTileEntity().getWorld();
        final int fortune = farm.getMaxLootingValue();
        List<ItemEntity> result = new ArrayList<>();

        NonNullList<ItemStack> nonNullDrops = NonNullList.create();
        nonNullDrops.addAll(Block.getDrops(state, (ServerWorld) world, pos, farm.asTileEntity()));

        float chance = ForgeEventFactory.fireBlockHarvesting(nonNullDrops, world, pos, state, fortune, 1.0F, false, null);
        farm.damageHoe(1, pos);
        boolean removed = false;

        for (ItemStack stack : nonNullDrops) {
            if (world.rand.nextFloat() <= chance) {
                if (!removed && stack.isItemEqual(getSeeds())) {
                    ItemStackUtil.decreaseStackSize(stack, 1);
                    removed = true;

                    if (ItemStackUtil.getStackSize(stack) > 0) {
                        result.add(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack.copy()));
                    }
                } else {
                    result.add(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack.copy()));
                }
            }
        }

        if (removed) {
            if (!plant(farm, world, pos, null)) {
                result.add(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, getSeeds().copy()));
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 1 | 2);
            }
        } else {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 1 | 2);
        }

        return new HarvestResult(result, pos);
    }

    protected boolean tillBlock(IFarm farm, BlockPos plantingLocation) {
        World world = farm.asTileEntity().getWorld();
        BlockPos dirtLoc = plantingLocation.offset(Direction.DOWN);
        Block dirtBlock = world.getBlockState(dirtLoc).getBlock();

        if (farm.isTillable(dirtBlock) && farm.hasHoe()) {
            farm.damageHoe(1, dirtLoc);
            world.setBlockState(dirtLoc, Blocks.FARMLAND.getDefaultState());

            SoundType soundType = Blocks.FARMLAND.getDefaultState().getSoundType();
            world.playSound(dirtLoc.getX() + 0.5F, dirtLoc.getY() + 0.5F, dirtLoc.getZ() + 0.5F,
                    SoundEvents.ITEM_CROP_PLANT, SoundCategory.BLOCKS,
                    soundType.volume, soundType.pitch, false);

            return true;
        }

        return false;
    }

    protected boolean isGroundTilled(IFarm farm, BlockPos plantingLocation) {
        return tilledBlocks.contains(farm.asTileEntity().getWorld().getBlockState(plantingLocation.offset(Direction.DOWN)).getBlock());
    }

    protected boolean canPlant(IFarm farm, World world, BlockPos pos) {
        BlockPos groundPos = pos.down();
        BlockState state = world.getBlockState(groundPos);
        Block ground = state.getBlock();
        IPlantable plantable = (IPlantable) getPlantedBlock();

        return state.isValidPosition(world, pos)
                && (ground.canSustainPlant(state, world, groundPos, Direction.UP, plantable) || ignoreSustainCheck)
                && (!checkGroundForFarmland || isGroundTilled(farm, pos));
    }

    protected boolean plant(IFarm farm, World world, BlockPos pos, ItemStack seed) {
        world.setBlockState(pos, Blocks.AIR.getDefaultState(), 1 | 2);

        if (canPlant(farm, world, pos)) {
            world.setBlockState(pos, getPlantedBlock().getDefaultState(), 1 | 2);

            if (seed != null) {
                ItemStackUtil.decreaseStackSize(seed, 1);
            }

            return true;
        }

        return false;
    }
}