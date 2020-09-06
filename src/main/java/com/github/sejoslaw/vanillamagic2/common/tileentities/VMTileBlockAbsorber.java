package com.github.sejoslaw.vanillamagic2.common.tileentities;

import com.github.sejoslaw.vanillamagic2.common.registries.TileEntityRegistry;
import com.github.sejoslaw.vanillamagic2.common.utils.BlockUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VMTileBlockAbsorber extends VMTileEntity {
    public VMTileBlockAbsorber() {
        super(TileEntityRegistry.BLOCK_ABSORBER.get());
    }

    public void tickTileEntity() {
        HopperTileEntity hopperTileEntity = (HopperTileEntity) this.getWorld().getTileEntity(this.getPos().offset(Direction.DOWN));

        if (hopperTileEntity == null) {
            this.removeTileEntity();
            return;
        }

        IInventory sourceInv = HopperTileEntity.getSourceInventory(hopperTileEntity);
        Block block = this.getWorld().getBlockState(this.getPos()).getBlock();

        if (sourceInv != null) {
            HopperTileEntity.pullItems(hopperTileEntity);
            return;
        }

        ItemEntity entity = new ItemEntity(this.getWorld(), this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), new ItemStack(block));
        this.getWorld().setBlockState(this.getPos(), Blocks.AIR.getDefaultState());
        HopperTileEntity.captureItem(hopperTileEntity, entity);
    }

    public static Block[] getValidBlocks() {
        return BlockUtils.getValidBlocks(new ArrayList<>(ForgeRegistries.BLOCKS.getValues()));
    }
}
