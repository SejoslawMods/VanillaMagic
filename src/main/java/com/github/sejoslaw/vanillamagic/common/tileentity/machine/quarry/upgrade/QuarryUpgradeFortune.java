package com.github.sejoslaw.vanillamagic.common.tileentity.machine.quarry.upgrade;

import com.github.sejoslaw.vanillamagic.api.tileentity.machine.quarry.IQuarryUpgrade;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuarryUpgradeFortune {
    public List<ItemStack> getDrops(World world, BlockPos workingPos, BlockState workingPosState, int fortune) {
        List<ItemStack> drops = Block.getDrops(workingPosState, (ServerWorld) world, workingPos, null);
        drops.forEach(stack -> stack.grow(fortune));
        return drops;
    }

    public static class One extends QuarryUpgradeFortune implements IQuarryUpgrade {
        public String getUpgradeName() {
            return "Fortune 1";
        }

        public Block getBlock() {
            return Blocks.LAPIS_BLOCK;
        }

        public List<ItemStack> getDrops(Block blockToDig, World world, BlockPos workingPos, BlockState workingPosState) {
            return super.getDrops(world, workingPos, workingPosState, 1);
        }
    }

    public static class Two extends QuarryUpgradeFortune implements IQuarryUpgrade {
        public String getUpgradeName() {
            return "Fortune 2";
        }

        public Block getBlock() {
            return Blocks.IRON_BLOCK;
        }

        public List<ItemStack> getDrops(Block blockToDig, World world, BlockPos workingPos, BlockState workingPosState) {
            return super.getDrops(world, workingPos, workingPosState, 2);
        }
    }

    public static class Three extends QuarryUpgradeFortune implements IQuarryUpgrade {
        public String getUpgradeName() {
            return "Fortune 3";
        }

        public Block getBlock() {
            return Blocks.GOLD_BLOCK;
        }

        public List<ItemStack> getDrops(Block blockToDig, World world, BlockPos workingPos, BlockState workingPosState) {
            return super.getDrops(world, workingPos, workingPosState, 3);
        }
    }
}