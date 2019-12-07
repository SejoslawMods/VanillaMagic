package com.github.sejoslaw.vanillamagic.tileentity.chunkloader;

import org.apache.logging.log4j.Level;

import com.github.sejoslaw.vanillamagic.api.event.EventChunkLoader;
import com.github.sejoslaw.vanillamagic.api.tileentity.chunkloader.IChunkLoader;
import com.github.sejoslaw.vanillamagic.core.VanillaMagic;
import com.github.sejoslaw.vanillamagic.tileentity.CustomTileEntity;
import com.github.sejoslaw.vanillamagic.util.BlockPosUtil;
import com.github.sejoslaw.vanillamagic.util.EventUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class TileChunkLoader extends CustomTileEntity implements IChunkLoader {
	public static final String REGISTRY_NAME = TileChunkLoader.class.getName();

	public void update() {
		if (!QuestChunkLoader.isChunkLoaderBuildCorrectly(world, this.pos)) {
			invalidate();
			VanillaMagic.log(Level.WARN, "Incorrect ChunkLoader placed on:");
			BlockPosUtil.printCoords(this.pos);
		}

		EventUtil.postEvent(new EventChunkLoader((IChunkLoader) this, world, pos));
	}
}