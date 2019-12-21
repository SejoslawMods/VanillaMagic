package com.github.sejoslaw.vanillamagic.api.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Unit;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerChunkProvider;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.Ticket;
import net.minecraft.world.server.TicketType;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic implementation of {@link ICustomTileEntity}.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class CustomTileEntityBase extends TileEntity implements ICustomTileEntity {
    protected Ticket chunkTicket;

    public CustomTileEntityBase(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public void init(World world, BlockPos pos) {
        this.world = world;
        this.pos = pos;
    }

    public TileEntity getTileEntity() {
        return this;
    }

    public void invalidate() {
        super.remove();
        stopChunkLoading();
    }

    /**
     * Stop chunkloading current CustomTileEntity
     */
    public void stopChunkLoading() {
        if (this.chunkTicket == null || !(this.world instanceof ServerWorld)) {
            return;
        }

        ((ServerChunkProvider) this.world.getChunkProvider()).func_217222_b(TicketType.START, this.getChunkPos(), 11, Unit.INSTANCE);

        this.chunkTicket = null;
    }

    public void forceChunkLoading(Ticket ticket) {
        if (chunkTicket == null) {
            chunkTicket = ticket;
        }

        if (this.world instanceof ServerWorld) {
            ((ServerChunkProvider) this.world.getChunkProvider()).func_217228_a(TicketType.START, this.getChunkPos(), 11, Unit.INSTANCE);
        }
    }

    public List<ITextComponent> getAdditionalInfo() {
        List<ITextComponent> list = new ArrayList<ITextComponent>();
        list.add(new StringTextComponent("CustomTileEntity name: " + getClass().getSimpleName()));
        list.add(new StringTextComponent("CustomTileEntity position: X=" + pos.getX() + ", Y=" + pos.getY() + ", Z=" + pos.getZ() + ", Dim=" + this.world.getDimension().getType().getId()));
        return list;
    }

    public Ticket getChunkTicket() {
        return chunkTicket;
    }

    public boolean prepareCustomTileEntity() {
        return true;
    }

    private ChunkPos getChunkPos() {
        return new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
    }
}