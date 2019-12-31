package com.github.sejoslaw.vanillamagic.api.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
import java.util.List;

/**
 * Basic implementation of {@link ICustomTileEntity}.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class CustomTileEntityBase extends TileEntity implements ICustomTileEntity {
    public CustomTileEntityBase(TileEntityType<?> tileEntityType) {
        super(tileEntityType);
    }

    public void setup(World world, BlockPos pos) {
        this.world = world;
        this.pos = pos;

        this.init();
    }

    public void init() {
        if (this.world instanceof ServerWorld) {
            IChunk chunk = world.getChunk(pos);
            ChunkPos chunkPos = chunk.getPos();
            this.world.getChunkProvider().forceChunk(chunkPos, true);
        }
    }

    public TileEntity asTileEntity() {
        return this;
    }

    public List<ITextComponent> getAdditionalInfo() {
        List<ITextComponent> list = new ArrayList<>();
        list.add(new StringTextComponent("CustomTileEntity name: " + getClass().getSimpleName()));
        list.add(new StringTextComponent("CustomTileEntity position: X=" + pos.getX() + ", Y=" + pos.getY() + ", Z=" + pos.getZ() + ", Dim=" + this.world.getDimension().getType().getId()));
        return list;
    }

    public boolean isPrepared() {
        return true;
    }
}