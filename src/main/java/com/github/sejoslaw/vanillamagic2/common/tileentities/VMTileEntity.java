package com.github.sejoslaw.vanillamagic2.common.tileentities;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class VMTileEntity extends TileEntity implements IVMTileEntity {
    public VMTileEntity(TileEntityType<? extends VMTileEntity> tileEntityType) {
        super(tileEntityType);
    }

    public TileEntity getTileEntity() {
        return this;
    }

    public SUpdateTileEntityPacket getUpdatePacket() {
        CompoundNBT nbt = this.getUpdateTag();
        return new SUpdateTileEntityPacket(getPos(), -999, nbt);
    }

    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        this.read(this.getState(), pkt.getNbtCompound());
    }

    public void tick() {
        if (this.getWorld().isRemote()) {
            return;
        }

        this.tickTileEntity();
    }

    protected abstract void tickTileEntity();
}
