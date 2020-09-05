package com.github.sejoslaw.vanillamagic2.common.tileentities;

import com.github.sejoslaw.vanillamagic2.common.registries.TileEntityRegistry;
import com.github.sejoslaw.vanillamagic2.common.utils.BlockUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.NbtUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VMTileInventoryBridge extends VMTileEntity {
    private long sourcePos;
    private int sourceDimId;

    private IInventory sourceInv;
    private IInventory destinationInv;

    public VMTileInventoryBridge() {
        super(TileEntityRegistry.INVENTORY_BRIDGE.get());
    }

    public void setSource(CompoundNBT nbt) {
        this.sourcePos = nbt.getLong(NbtUtils.NBT_POSITION);
        this.sourceDimId = nbt.getInt(NbtUtils.NBT_DIMENSION);
        this.destinationInv = WorldUtils.getInventory(this.getWorld(), this.getPos().offset(Direction.DOWN));

        DimensionType sourceDimType = DimensionType.getById(this.sourceDimId);
        ServerWorld sourceWorld = DimensionManager.getWorld(this.getWorld().getServer(), sourceDimType, false, true);
        this.sourceInv = WorldUtils.getInventory(sourceWorld, BlockPos.fromLong(this.sourcePos));
    }

    public void read(CompoundNBT compound) {
        super.read(compound);
        this.setSource(compound);
    }

    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.putLong(NbtUtils.NBT_POSITION, this.sourcePos);
        compound.putInt(NbtUtils.NBT_DIMENSION, this.sourceDimId);
        return compound;
    }

    public void tick() {
        if (this.destinationInv == null || this.sourceInv == null) {
            this.remove();
            return;
        }

        for (int i = 0; i < this.sourceInv.getSizeInventory(); ++i) {
            HopperTileEntity.putStackInInventoryAllSlots(this.sourceInv, this.destinationInv, this.sourceInv.getStackInSlot(i), null);
        }
    }

    public static Block[] getValidBlocks() {
        List<Block> blocks = ForgeRegistries.BLOCKS
                .getValues()
                .stream()
                .filter(block -> block.hasTileEntity(block.getDefaultState()) &&
                        block.createTileEntity(block.getDefaultState(), null) instanceof IInventory)
                .collect(Collectors.toList());

        return BlockUtils.getValidBlocks(blocks);
    }
}
