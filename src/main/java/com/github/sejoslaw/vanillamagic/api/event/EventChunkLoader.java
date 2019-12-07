package com.github.sejoslaw.vanillamagic.api.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import com.github.sejoslaw.vanillamagic.api.tileentity.chunkloader.IChunkLoader;

/**
 * This Event is fired at the end of TileChunkLoader tick.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventChunkLoader extends EventCustomTileEntity {
    private final IChunkLoader tileChunkLoader;

    public EventChunkLoader(IChunkLoader tileChunkLoader, World world, BlockPos customTilePos) {
        super(tileChunkLoader, world, customTilePos);
        this.tileChunkLoader = tileChunkLoader;
    }

    /**
     * @return Returns the TileChunkLoader.
     */
    public IChunkLoader getTileChunkLoader() {
        return tileChunkLoader;
    }
}