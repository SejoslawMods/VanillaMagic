package com.github.sejoslaw.vanillamagic.common.tileentity;

import com.github.sejoslaw.vanillamagic.api.tileentity.CustomTileEntityBase;
import com.github.sejoslaw.vanillamagic.common.util.NBTUtil;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class CustomTileEntity extends CustomTileEntityBase {
    public CustomTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    /**
     * Try to override {@link #serializeNBT()} instead of this method.
     */
    @Deprecated
    public CompoundNBT write(CompoundNBT tag) {
        super.write(tag);
        tag.putString(NBTUtil.NBT_CLASS, this.getClass().getName());
        return tag;
    }

    public TileEntity getTileEntity() {
        return this;
    }

    public void deserializeNBT(CompoundNBT nbt) {
    }

    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        return tag;
    }

    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbt = this.getUpdateTag();
        return new SUpdateTileEntityPacket(getPos(), -999, nbt);
    }

    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        this.read(pkt.getNbtCompound());
    }

    public void handleUpdateTag(CompoundNBT tag) {
    }

    public void onChunkUnloaded() {
    }

    public void onLoad() {
    }

    public AxisAlignedBB getRenderBoundingBox() {
        return null;
    }

    public boolean canRenderBreaking() {
        return false;
    }

    public boolean hasFastRenderer() {
        return false;
    }

    public void requestModelDataUpdate() {
    }

    public IModelData getModelData() {
        return null;
    }

    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    public void tick() {
    }

    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap) {
        return null;
    }
}