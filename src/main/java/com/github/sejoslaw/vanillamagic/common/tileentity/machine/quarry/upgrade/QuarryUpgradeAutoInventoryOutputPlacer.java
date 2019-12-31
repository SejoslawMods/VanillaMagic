package com.github.sejoslaw.vanillamagic.common.tileentity.machine.quarry.upgrade;

import com.github.sejoslaw.vanillamagic.api.exception.NotInventoryException;
import com.github.sejoslaw.vanillamagic.api.inventory.IInventoryWrapper;
import com.github.sejoslaw.vanillamagic.api.tileentity.machine.quarry.IQuarry;
import com.github.sejoslaw.vanillamagic.api.tileentity.machine.quarry.IQuarryUpgrade;
import com.github.sejoslaw.vanillamagic.common.inventory.InventoryHelper;
import com.github.sejoslaw.vanillamagic.common.util.BlockUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuarryUpgradeAutoInventoryOutputPlacer implements IQuarryUpgrade {
    public String getUpgradeName() {
        return "Auto Inventory Output Placer";
    }

    public Block getBlock() {
        return Blocks.WHITE_STAINED_GLASS_PANE;
    }

    public void modifyQuarry(IQuarry quarry) {
        IInventoryWrapper invOutputWrapper = quarry.getOutputInventory();
        IInventory invOutput = invOutputWrapper.getInventory();
        Direction invFacing = quarry.getOutputDirection();

        if (InventoryHelper.hasInventoryFreeSpace(invOutput, invFacing)) {
            return;
        }

        IInventoryWrapper invInputWrapper = quarry.getInputInventory();
        int slotWithInventory = InventoryHelper.containsAnotherInventoryBlock(invInputWrapper);

        if (slotWithInventory == -1) {
            return;
        }

        World world = invOutputWrapper.getWorld();
        BlockPos nextPos = invOutputWrapper.getPos().offset(invFacing, 2);
        IInventory invInput = invInputWrapper.getInventory();
        ItemStack stackWithInventory = invInput.getStackInSlot(slotWithInventory);

        BlockUtil.placeBlockFromStack(world, nextPos, stackWithInventory);

        invInput.decrStackSize(slotWithInventory, 1);
        BlockState placedBlockState = world.getBlockState(nextPos);
        Block placedBlock = placedBlockState.getBlock();

        if (!(placedBlock instanceof ITileEntityProvider)) {
            return;
        }

        TileEntity createdTileEntity = placedBlock.createTileEntity(placedBlockState, world);
        world.setTileEntity(nextPos, createdTileEntity);

        try {
            invOutputWrapper.setNewInventory(world, nextPos);
        } catch (NotInventoryException e) {
            e.printStackTrace();
        }
    }
}