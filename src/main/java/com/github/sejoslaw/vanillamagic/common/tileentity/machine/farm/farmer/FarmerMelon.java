package com.github.sejoslaw.vanillamagic.common.tileentity.machine.farm.farmer;

import com.github.sejoslaw.vanillamagic.api.tileentity.machine.farm.IFarm;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class FarmerMelon extends FarmerCustomSeed {
    private Block grownBlock;

    public FarmerMelon(Block plantedBlock, Block grownBlock, ItemStack seeds) {
        super(plantedBlock, seeds);

        this.grownBlock = grownBlock;
    }

    public boolean prepareBlock(IFarm farm, BlockPos pos, Block block, BlockState state) {
        int xVal = farm.asTileEntity().getPos().getX() & 1;
        int zVal = farm.asTileEntity().getPos().getZ() & 1;

        if ((pos.getX() & 1) != xVal || (pos.getZ() & 1) != zVal) {
            IInventory inv = farm.getInputInventory().getInventory();

            for (int i = 0; i < inv.getSizeInventory(); ++i) {
                ItemStack invSeeds = inv.getStackInSlot(i);

                if (canPlant(invSeeds)) {
                    return true;
                }
            }
        }

        return super.prepareBlock(farm, pos, block, state);
    }

    public boolean canHarvest(IFarm farm, BlockPos pos, Block block, BlockState state) {
        return block == grownBlock;
    }

    public int getMaxAge() {
        return 0;
    }

    public int getAge(BlockState state) {
        return 0;
    }
}