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
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VMTileBlockAbsorber extends VMTileEntity {
    private HopperTileEntity hopperTileEntity;

    public VMTileBlockAbsorber() {
        super(VMTiles.BLOCK_ABSORBER);
    }

    public void initialize(World world, BlockPos pos) {
        super.initialize(world, pos);

        this.hopperTileEntity = (HopperTileEntity) world.getTileEntity(this.getPos().offset(Direction.DOWN));

        if (this.hopperTileEntity == null) {
            this.remove();
        }
    }

    public void tick() {
        for (int i = 0; i < VMForgeConfig.TILE_ABSORBER_PULLING_SPEED.get(); ++i) {
            if (!HopperTileEntity.pullItems(this.hopperTileEntity)) {
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
        HopperTileEntity.pullItems(this.hopperTileEntity);
    }
}
