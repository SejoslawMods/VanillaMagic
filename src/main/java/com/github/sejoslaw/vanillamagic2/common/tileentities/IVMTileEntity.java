package com.github.sejoslaw.vanillamagic2.common.tileentities;

import net.minecraft.block.BlockState;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunk;
import net.minecraftforge.common.extensions.IForgeTileEntity;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface IVMTileEntity extends ITickableTileEntity, IForgeTileEntity {
    /**
     * @return Update data packet with data about the current VM TileEntity.
     */
    SUpdateTileEntityPacket getUpdatePacket();

    /**
     * Adds information about the current VM TileEntity.
     */
    default void addInformation(List<ITextComponent> lines) {
    }

    /**
     * Initializes the current VM TileEntity.
     */
    default void initialize(IWorld world, BlockPos pos) {
        this.getTileEntity().setWorldAndPos((World)world, pos);
        this.getWorld().getChunkProvider().forceChunk(this.getChunkPos(), true);
    }

    /**
     * @return IWorld of the current VM TileEntity.
     */
    default IWorld getWorld() {
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
    default IChunk getChunk() {
        return this.getWorld().getChunk(this.getPos());
    }

    /**
     * @return X and Z coordinates of a Chunk in which the current VM TileEntity is.
     */
    default ChunkPos getChunkPos() {
        return this.getChunk().getPos();
    }

    /**
     * @return BlockState of the current VM TileEntity.
     */
    default BlockState getState() {
        return this.getTileEntity().getBlockState();
    }

    /**
     * Removes TileEntity and cleans up registry.
     */
    default void removeTileEntity() {
        this.getTileEntity().getWorld().removeTileEntity(this.getPos());
        this.getTileEntity().remove();
    }

    /**
     * Performs logic needed to add current VM TileEntity into the IWorld.
     */
    default void spawn() {
        this.getTileEntity().getWorld().addTileEntity(this.getTileEntity());
    }
}
