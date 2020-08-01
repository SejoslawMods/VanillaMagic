package com.github.sejoslaw.vanillamagic2.common.tileentities;

import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.extensions.IForgeTileEntity;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface IVMTileEntity extends ITickableTileEntity, IForgeTileEntity {
    /**
     * @return Current VM TileEntity in a form of a Minecraft standard TileEntity.
     */
    TileEntity getTileEntity();

    /**
     * @return Update data packet with data about the current VM TileEntity.
     */
    SUpdateTileEntityPacket getUpdatePacket();

    /**
     * Called when you receive a TileEntityData packet for the location this
     * TileEntity is currently in. On the client, the NetworkManager will always
     * be the remote server. On the server, it will be whomever is responsible for
     * sending the packet.
     */
    void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt);

    /**
     * Adds tooltip information about the current VM TileEntity.
     */
    default void addTooltipInfo(List<ITextComponent> lines) {
    }

    /**
     * Ticking functionality for Vm TileEntity.
     */
    default void tick() {
    }

    /**
     * Initializes the current VM TileEntity.
     */
    default void initialize(World world, BlockPos pos) {
        this.getTileEntity().setWorldAndPos(world, pos);
        this.getWorld().getChunkProvider().forceChunk(this.getChunkPos(), true);
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
    default void remove() {
        this.getWorld().removeTileEntity(this.getPos());
        TextUtils.addChatMessage("tile.removed");
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

    /**
     * Performs logic needed to add current VM TileEntity into the World.
     */
    default void spawn() {
        this.getWorld().addTileEntity(this.getTileEntity());
        TextUtils.addChatMessage("tile.added");
    }
}
