package seia.vanillamagic;

import net.minecraftforge.fml.common.registry.GameRegistry;
import seia.vanillamagic.chunkloader.TileChunkLoader;
import seia.vanillamagic.machine.autocrafting.TileAutocrafting;
import seia.vanillamagic.machine.farm.TileFarm;
import seia.vanillamagic.machine.quarry.TileQuarry;

public class VanillaMagicRegistry 
{
	public static final VanillaMagicRegistry INSTANCE = new VanillaMagicRegistry();
	
	private VanillaMagicRegistry()
	{
	}
	
	public void preInit()
	{
		// Register TileEntities
		GameRegistry.registerTileEntity(TileQuarry.class, TileQuarry.REGISTRY_NAME);
		GameRegistry.registerTileEntity(TileChunkLoader.class, TileChunkLoader.REGISTRY_NAME);
		GameRegistry.registerTileEntity(TileFarm.class, TileFarm.REGISTRY_NAME);
		GameRegistry.registerTileEntity(TileAutocrafting.class, TileAutocrafting.REGISTRY_NAME);
	}
}