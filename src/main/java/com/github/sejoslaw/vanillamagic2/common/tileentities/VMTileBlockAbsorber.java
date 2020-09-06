package com.github.sejoslaw.vanillamagic2.common.tileentities;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.registries.TileEntityRegistry;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.util.Direction;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VMTileBlockAbsorber extends VMTileEntity {
    public VMTileBlockAbsorber() {
        super(TileEntityRegistry.BLOCK_ABSORBER.get());
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

        IInventory inv = WorldUtils.getInventory(this.getWorld(), this.getPos());

        if (inv == null || !inv.isEmpty()) {
            return;
        }

        Block.spawnAsEntity(this.getWorld(), this.getPos(), new ItemStack(this.getBlockState().getBlock()));
        this.getWorld().setBlockState(this.getPos(), Blocks.AIR.getDefaultState());
        HopperTileEntity.pullItems(hopperTileEntity);
    }

    public static Block[] getValidBlocks() {
        return BlockUtils.getValidBlocks(new ArrayList<>(ForgeRegistries.BLOCKS.getValues()));
    }
}
