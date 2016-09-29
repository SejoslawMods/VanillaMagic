package seia.vanillamagic;

import net.minecraftforge.fml.common.registry.GameRegistry;
import seia.vanillamagic.chunkloader.TileChunkLoader;
import seia.vanillamagic.item.liquidsuppressioncrystal.TileLiquidSuppression;
import seia.vanillamagic.machine.autocrafting.TileAutocrafting;
import seia.vanillamagic.machine.farm.TileFarm;
import seia.vanillamagic.machine.quarry.TileQuarry;
import seia.vanillamagic.machine.speedy.TileSpeedy;

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
		GameRegistry.registerTileEntity(TileSpeedy.class, TileSpeedy.REGISTRY_NAME);
		GameRegistry.registerTileEntity(TileLiquidSuppression.class, TileLiquidSuppression.REGISTRY_NAME);
	}
}