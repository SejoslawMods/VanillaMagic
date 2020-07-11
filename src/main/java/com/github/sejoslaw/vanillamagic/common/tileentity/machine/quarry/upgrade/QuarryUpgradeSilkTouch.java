package com.github.sejoslaw.vanillamagic.common.tileentity.machine.quarry.upgrade;

import com.github.sejoslaw.vanillamagic.api.tileentity.machine.quarry.IQuarryUpgrade;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuarryUpgradeSilkTouch implements IQuarryUpgrade {
    public String getUpgradeName() {
        return "Silk-Touch";
    }

    public Block getBlock() {
        return Blocks.QUARTZ_BLOCK;
    }

    public List<ItemStack> getDrops(Block blockToDig, World world, BlockPos workingPos, BlockState workingPosState) {
        List<ItemStack> list = new ArrayList<>();
        list.add(new ItemStack(blockToDig));
        return list;
    }
}
