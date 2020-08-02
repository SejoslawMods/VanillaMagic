package com.github.sejoslaw.vanillamagic2.common.tileentities;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.core.VMTiles;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VMTileBlockAbsorber extends VMTileEntity {
    public VMTileBlockAbsorber() {
        super(VMTiles.BLOCK_ABSORBER);
    }

    public void tick() {
        HopperTileEntity hopperTileEntity = (HopperTileEntity) this.getWorld().getTileEntity(this.getPos().offset(Direction.DOWN));

        if (hopperTileEntity == null) {
            this.remove();
        }

        for (int i = 0; i < VMForgeConfig.TILE_ABSORBER_PULLING_SPEED.get(); ++i) {
            if (!HopperTileEntity.pullItems(hopperTileEntity)) {
                return;
            }
        }

        TileEntity tile = this.getWorld().getTileEntity(this.getPos());

        if (tile instanceof IInventory && !((IInventory) tile).isEmpty()) {
            return;
        }

        ItemEntity itemEntity = new ItemEntity(this.getWorld(), this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), new ItemStack(this.getBlockState().getBlock()));
        this.getWorld().setBlockState(this.getPos(), Blocks.AIR.getDefaultState());
        this.getWorld().addEntity(itemEntity);
        HopperTileEntity.pullItems(hopperTileEntity);
    }
}
