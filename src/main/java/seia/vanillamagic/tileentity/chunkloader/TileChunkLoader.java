package seia.vanillamagic.tileentity.chunkloader;

import org.apache.logging.log4j.Level;

import seia.vanillamagic.api.event.EventChunkLoader;
import seia.vanillamagic.api.tileentity.chunkloader.IChunkLoader;
import seia.vanillamagic.core.VanillaMagic;
import seia.vanillamagic.tileentity.CustomTileEntity;
import seia.vanillamagic.util.BlockPosUtil;
import seia.vanillamagic.util.EventUtil;

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