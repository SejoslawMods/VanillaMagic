package com.github.sejoslaw.vanillamagic.common.tileentity.chunkloader;

import com.github.sejoslaw.vanillamagic.api.event.EventChunkLoader;
import com.github.sejoslaw.vanillamagic.api.tileentity.chunkloader.IChunkLoader;
import com.github.sejoslaw.vanillamagic.common.tileentity.CustomTileEntity;
import com.github.sejoslaw.vanillamagic.common.util.BlockPosUtil;
import com.github.sejoslaw.vanillamagic.common.util.EventUtil;
import com.github.sejoslaw.vanillamagic.core.VMLogger;
import com.github.sejoslaw.vanillamagic.core.VMTileEntities;
import org.apache.logging.log4j.Level;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class TileChunkLoader extends CustomTileEntity implements IChunkLoader {
    public static final String REGISTRY_NAME = TileChunkLoader.class.getName();

    public TileChunkLoader() {
        super(VMTileEntities.CHUNK_LOADER);
    }

    public void tick() {
        if (!QuestChunkLoader.isChunkLoaderBuildCorrectly(world, this.pos)) {
            remove();
            VMLogger.log(Level.WARN, "Incorrect ChunkLoader placed on:");
            BlockPosUtil.printCoords(this.pos);
        }

        EventUtil.postEvent(new EventChunkLoader(this, world, pos));
    }
}