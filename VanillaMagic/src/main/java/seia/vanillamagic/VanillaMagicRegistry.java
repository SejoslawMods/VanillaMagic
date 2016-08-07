package seia.vanillamagic;

import net.minecraftforge.fml.common.registry.GameRegistry;
import seia.vanillamagic.chunkloader.TileChunkLoader;
import seia.vanillamagic.machine.quarry.TileQuarry;

public class VanillaMagicRegistry 
{
	public static final VanillaMagicRegistry INSTANCE = new VanillaMagicRegistry();
	
	private VanillaMagicRegistry()
	{
	}
	
	public void preInit()
	{
		// Register tileEntities
		GameRegistry.registerTileEntity(TileQuarry.class, TileQuarry.TILE_QUARRY_NAME);
		GameRegistry.registerTileEntity(TileChunkLoader.class, TileChunkLoader.TILE_CHUNK_LOADER_NAME);
	}
}