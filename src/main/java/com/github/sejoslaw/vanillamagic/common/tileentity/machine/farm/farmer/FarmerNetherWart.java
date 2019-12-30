package com.github.sejoslaw.vanillamagic.common.tileentity.machine.farm.farmer;

import com.github.sejoslaw.vanillamagic.api.tileentity.machine.farm.IFarm;
import net.minecraft.block.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

import java.util.Comparator;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class FarmerNetherWart extends FarmerCustomSeed {
    public FarmerNetherWart() {
        super(Blocks.NETHER_WART, new ItemStack(Items.NETHER_WART));
    }

    public boolean prepareBlock(IFarm farm, BlockPos pos, Block block, BlockState state) {
        if (!farm.isAir(pos)) {
            return false;
        }

        return plantFromInventory(farm, pos);
    }

    public int getMaxAge() {
        return NetherWartBlock.AGE.getAllowedValues().stream().max(Comparator.naturalOrder()).get();
    }

    public int getAge(BlockState state) {
        return state.get(NetherWartBlock.AGE);
    }
}