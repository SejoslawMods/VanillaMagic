package seia.vanillamagic.tileentity.chunkloader;

import org.apache.logging.log4j.Level;

import net.minecraftforge.common.MinecraftForge;
import seia.vanillamagic.api.event.EventChunkLoader;
import seia.vanillamagic.api.tileentity.chunkloader.IChunkLoader;
import seia.vanillamagic.core.VanillaMagic;
import seia.vanillamagic.tileentity.CustomTileEntity;
import seia.vanillamagic.util.BlockPosHelper;

public class TileChunkLoader extends CustomTileEntity implements IChunkLoader
{
	public static final String REGISTRY_NAME = TileChunkLoader.class.getName();
	
	public void update() 
	{
		if(!QuestChunkLoader.isChunkLoaderBuildCorrectly(world, this.pos))
		{
			invalidate();
			VanillaMagic.LOGGER.log(Level.WARN, "Incorrect ChunkLoader placed on:");
			BlockPosHelper.printCoords(this.pos);
		}
		MinecraftForge.EVENT_BUS.post(new EventChunkLoader((IChunkLoader) this, world, pos));
	}
}