package com.github.sejoslaw.vanillamagic.common.tileentity.machine.farm.farmer;

import com.github.sejoslaw.vanillamagic.api.tileentity.machine.farm.IFarm;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class FarmerOreDictionaryTree extends FarmerTree {
    protected List<ItemStack> saplings;
    protected List<ItemStack> woodBlocks;

    public FarmerOreDictionaryTree(List<ItemStack> saplings, List<ItemStack> woods) {
        super(null);
        this.saplings = saplings;
        this.woodBlocks = woods;
    }

    protected boolean isWood(Block block) {
        return woodBlocks.contains(block);
    }

    public boolean canPlant(ItemStack stack) {
        return stack != null && saplings.contains(stack) && Block.getBlockFromItem(stack.getItem()) != Blocks.AIR;
    }

    public boolean prepareBlock(IFarm farm, BlockPos pos, Block block, BlockState state) {
        if (saplings.contains(block)) {
            return true;
        }

        return plantFromInventory(farm, pos, block, state);
    }

    protected boolean plantFromInventory(IFarm farm, BlockPos pos, Block block, BlockState state) {
        World world = farm.asTileEntity().getWorld();
        ItemStack saplingStack = new ItemStack(null);
        IInventory inv = farm.getInputInventory().getInventory();

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack invStack = inv.getStackInSlot(i);

            if ((!ItemStackUtil.isNullStack(invStack)) && saplings.contains(invStack)) {
                saplingStack = invStack.copy();
                break;
            }
        }

        if (canPlant(world, pos, saplingStack)) {
            ItemStack seed = farm.takeSeedFromSupplies(saplingStack, pos);

            if (!ItemStackUtil.isNullStack(seed)) {
                return plant(farm, world, pos, seed);
            }
        }

        return false;
    }

    protected boolean canPlant(World world, BlockPos pos, ItemStack saplingStack) {
        if (!saplings.contains(saplingStack) || ItemStackUtil.isNullStack(saplingStack)) {
            return false;
        }

        BlockPos groundPos = pos.down();
        BlockState groundState = world.getBlockState(groundPos);
        Block groundBlock = groundState.getBlock();
        Block saplingBlock = Block.getBlockFromItem(saplingStack.getItem());

        if (saplingBlock == Blocks.AIR) {
            return false;
        }

        return groundBlock.canSustainPlant(groundState, world, groundPos, Direction.UP, (IPlantable) saplingBlock);
    }

    protected boolean plant(IFarm farm, World world, BlockPos pos, ItemStack seedStack) {
        world.setBlockState(pos, Block.getBlockFromItem(seedStack.getItem()).getDefaultState(), 1 | 2);

        if (!ItemStackUtil.isNullStack(seedStack)) {
            ItemStackUtil.decreaseStackSize(seedStack, 1);
        }

        return true;
    }
}