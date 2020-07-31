package com.github.sejoslaw.vanillamagic2.common.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface IVMTileEntity extends ITickableTileEntity {
    /**
     * @return Current VM TileEntity in a form of a Minecraft standard TileEntity.
     */
    TileEntity getTileEntity();

    /**
     * Adds tooltip information about the current VM TileEntity.
     */
    default void addTooltipInfo(List<ITextComponent> lines) {
    }

    /**
     * Initializes the current VM TileEntity.
     */
    default void initialize(World world, BlockPos pos) {
        this.getTileEntity().setWorldAndPos(world, pos);
    }

    /**
     * @return World of the current VM TileEntity.
     */
    default World getWorld() {
        return this.getTileEntity().getWorld();
    }

    /**
     * @return Position of the current VM TileEntity.
     */
    default BlockPos getPos() {
        return this.getTileEntity().getPos();
    }

    /**
     * @return Chunk to which the current VM TileEntity belongs.
     */
    default Chunk getChunk() {
        return this.getWorld().getChunkAt(this.getPos());
    }

    /**
     * @return BlockState of the current VM TileEntity.
     */
    default BlockState getState() {
        return this.getTileEntity().getBlockState();
    }

    /**
     * Removes corresponding TileEntity.
     */
    default void remove() {
        this.getTileEntity().remove();
    }

    /**
     * Reads data from the given NBT object.
     */
    default void read(CompoundNBT nbt) {
        this.getTileEntity().read(nbt);
    }

    /**
     * @return NBT object with written data corresponded with this VM TileEntity.
     */
    default CompoundNBT write(CompoundNBT nbt) {
        return this.getTileEntity().write(nbt);
    }
}
