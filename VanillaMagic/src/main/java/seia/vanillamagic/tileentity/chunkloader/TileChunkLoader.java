package seia.vanillamagic.tileentity.chunkloader;

import org.apache.logging.log4j.Level;

import seia.vanillamagic.api.tileentity.chunkloader.IChunkLoader;
import seia.vanillamagic.core.VanillaMagic;
import seia.vanillamagic.tileentity.CustomTileEntity;
import seia.vanillamagic.util.BlockPosHelper;

public class TileChunkLoader extends CustomTileEntity implements IChunkLoader
{
	public static final String REGISTRY_NAME = TileChunkLoader.class.getSimpleName();
	
	public void update() 
	{
		if(!QuestChunkLoader.isChunkLoaderBuildCorrectly(worldObj, this.pos))
		{
			invalidate();
			VanillaMagic.LOGGER.log(Level.WARN, "Incorrect ChunkLoader placed on:");
			BlockPosHelper.printCoords(this.pos);
		}
	}
}