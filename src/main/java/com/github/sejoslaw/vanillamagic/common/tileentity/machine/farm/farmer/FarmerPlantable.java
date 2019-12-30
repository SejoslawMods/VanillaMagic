package com.github.sejoslaw.vanillamagic.common.tileentity.machine.farm.farmer;

import com.github.sejoslaw.vanillamagic.api.tileentity.machine.farm.IFarm;
import com.github.sejoslaw.vanillamagic.api.tileentity.machine.farm.IFarmer;
import com.github.sejoslaw.vanillamagic.api.tileentity.machine.farm.IHarvestResult;
import com.github.sejoslaw.vanillamagic.common.tileentity.machine.farm.HarvestResult;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import net.minecraft.block.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class FarmerPlantable implements IFarmer {
    public int getDefaultAge() {
        return 0;
    }

    public int getMaxAge() {
        return 0;
    }

    public int getAge(BlockState state) {
        return 0;
    }

    public boolean canPlant(ItemStack stack) {
        if (ItemStackUtil.isNullStack(stack) || (ItemStackUtil.getStackSize(stack) <= 0)) {
            return false;
        }

        return stack.getItem() instanceof IPlantable;
    }

    public boolean prepareBlock(IFarm farm, BlockPos pos, Block block, BlockState state) {
        if (block == null) {
            return false;
        }

        ItemStack seedStack = ItemStackUtil.NULL_STACK;

        IInventory inventory = farm.getInputInventory().getInventory();

        for (int i = 0; i < inventory.getSizeInventory(); ++i) {
            ItemStack inventoryStack = inventory.getStackInSlot(i);

            if (!ItemStackUtil.isNullStack(inventoryStack) && inventoryStack.getItem() instanceof IPlantable) {
                seedStack = inventoryStack;
                break;
            }
        }

        if (ItemStackUtil.isNullStack(seedStack)) {
            return false;
        }

        IPlantable plantable = (IPlantable) seedStack.getItem();
        PlantType plantType = plantable.getPlantType(farm.asTileEntity().getWorld(), pos);
        World world = farm.asTileEntity().getWorld();
        Block ground = world.getBlockState(pos.offset(Direction.DOWN)).getBlock();

        if (plantType == PlantType.Nether) {
            if (ground != Blocks.SOUL_SAND) {
                return false;
            }

            return plantFromInventory(farm, pos, seedStack);
        }

        if (plantType == PlantType.Crop) {
            farm.tillBlock(pos);
            return plantFromInventory(farm, pos, seedStack);
        }

        if (plantType == PlantType.Water) {
            return plantFromInventory(farm, pos, seedStack);
        }

        return false;
    }

    protected boolean plantFromInventory(IFarm farm, BlockPos bc, ItemStack plantable) {
        World world = farm.asTileEntity().getWorld();

        if (canPlant(world, bc, plantable)) {
            return plant(farm, world, bc, plantable);
        }

        return false;
    }

    protected boolean plant(IFarm farm, World world, BlockPos bc, ItemStack stack) {
        if (ItemStackUtil.getStackSize(stack) <= 0) {
            return false;
        }

        world.setBlockState(bc, Blocks.AIR.getDefaultState(), 1 | 2);
        BlockState target = ((IPlantable) stack.getItem()).getPlant(null, new BlockPos(0, 0, 0));
        world.setBlockState(bc, target, 1 | 2);

        if (!ItemStackUtil.isNullStack(stack)) {
            ItemStackUtil.decreaseStackSize(stack, 1);

            if (ItemStackUtil.getStackSize(stack) == 0) {
                farm.clearInputInventory();
            }
        }

        return true;
    }

    protected boolean canPlant(World world, BlockPos pos, ItemStack stack) {
        if (ItemStackUtil.getStackSize(stack) <= 0) {
            return false;
        }

        BlockState target = ((IPlantable) stack.getItem()).getPlant(null, new BlockPos(0, 0, 0));
        BlockPos groundPos = pos.down();
        BlockState groundState = world.getBlockState(groundPos);
        Block groundBlock = groundState.getBlock();

        return (target != null && groundBlock.canSustainPlant(groundState, world, groundPos, Direction.UP, ((IPlantable) stack.getItem())));
    }

    public boolean canHarvest(IFarm farm, BlockPos bc, Block block, BlockState state) {
        if (block instanceof IGrowable && !(block instanceof StemBlock)) {
            return !((IGrowable) block).canGrow(farm.asTileEntity().getWorld(), bc, state, true);
        }

        return false;
    }

    public IHarvestResult harvestBlock(IFarm farm, BlockPos pos, Block block, BlockState state) {
        if (!canHarvest(farm, pos, block, state) || !farm.hasHoe()) {
            return null;
        }

        World world = farm.asTileEntity().getWorld();
        List<ItemEntity> result = new ArrayList<>();
        int fortune = farm.getMaxLootingValue();
        ItemStack removedPlantable = null;

        NonNullList<ItemStack> nonNullDrops = NonNullList.create();
        nonNullDrops.addAll(Block.getDrops(state, (ServerWorld) world, pos, farm.asTileEntity()));

        float chance = ForgeEventFactory.fireBlockHarvesting(nonNullDrops, world, pos, state, fortune, 1.0F, false, null);
        farm.damageHoe(1, pos);
        boolean removed = false;

        for (ItemStack stack : nonNullDrops) {
            if (stack != null && ItemStackUtil.getStackSize(stack) > 0 && world.rand.nextFloat() <= chance) {
                if (!removed && isPlantableForBlock(stack, block)) {
                    removed = true;
                    removedPlantable = stack.copy();
                    ItemStackUtil.setStackSize(removedPlantable, 1);
                    ItemStackUtil.decreaseStackSize(stack, 1);

                    if (ItemStackUtil.getStackSize(stack) > 0) {
                        result.add(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack.copy()));
                    }
                } else {
                    result.add(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, stack.copy()));
                }
            }
        }

        if (removed) {
            if (!plant(farm, world, pos, removedPlantable)) {
                result.add(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, removedPlantable.copy()));
                world.setBlockState(pos, Blocks.AIR.getDefaultState(), 1 | 2);
            }
        } else {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), 1 | 2);
        }

        return new HarvestResult(result, pos);
    }

    private boolean isPlantableForBlock(ItemStack stack, Block block) {
        if (!(stack.getItem() instanceof IPlantable)) {
            return false;
        }

        IPlantable plantable = (IPlantable) stack.getItem();
        BlockState b = plantable.getPlant(null, new BlockPos(0, 0, 0));

        return b != null && b.getBlock() == block;
    }
}