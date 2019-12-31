package com.github.sejoslaw.vanillamagic.common.tileentity.machine.quarry.upgrade;

import com.github.sejoslaw.vanillamagic.api.tileentity.machine.quarry.IQuarryUpgrade;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import com.github.sejoslaw.vanillamagic.common.util.SmeltingUtil;
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
public class QuarryUpgradeAutosmelt implements IQuarryUpgrade {
    public String getUpgradeName() {
        return "Autosmelt";
    }

    public Block getBlock() {
        return Blocks.FURNACE;
    }

    public List<ItemStack> getDrops(Block blockToDig, World world, BlockPos workingPos, BlockState workingPosState) {
        List<ItemStack> list = new ArrayList<>();
        ItemStack burnedStack = SmeltingUtil.getSmeltingResultAsNewStack(new ItemStack(blockToDig), world);

        if (!ItemStackUtil.isNullStack(burnedStack)) {
            list.add(burnedStack);
        }

        return list;
    }
}