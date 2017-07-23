package seia.vanillamagic.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import seia.vanillamagic.item.liquidsuppressioncrystal.TileLiquidSuppression;
import seia.vanillamagic.tileentity.blockabsorber.TileBlockAbsorber;
import seia.vanillamagic.tileentity.chunkloader.TileChunkLoader;
import seia.vanillamagic.tileentity.inventorybridge.TileInventoryBridge;
import seia.vanillamagic.tileentity.machine.autocrafting.TileAutocrafting;
import seia.vanillamagic.tileentity.machine.farm.TileFarm;
import seia.vanillamagic.tileentity.machine.quarry.TileQuarry;
import seia.vanillamagic.tileentity.speedy.TileSpeedy;

/**
 * Class which is used to register VM CustomTileEntities.
 */
public class TileEntityRegistry 
{
	private TileEntityRegistry()
	{
	}
	
	public static void preInit()
	{
		// Register VM CustomTileEntities
		register(TileQuarry.class, TileQuarry.REGISTRY_NAME);
		register(TileChunkLoader.class, TileChunkLoader.REGISTRY_NAME);
		register(TileFarm.class, TileFarm.REGISTRY_NAME);
		register(TileAutocrafting.class, TileAutocrafting.REGISTRY_NAME);
		register(TileSpeedy.class, TileSpeedy.REGISTRY_NAME);
		register(TileLiquidSuppression.class, TileLiquidSuppression.REGISTRY_NAME);
		register(TileBlockAbsorber.class, TileBlockAbsorber.REGISTRY_NAME);
		register(TileInventoryBridge.class, TileInventoryBridge.REGISTRY_NAME);
	}
	
	private static void register(Class<? extends TileEntity> tileEntityClass, String id)
	{
		GameRegistry.registerTileEntity(tileEntityClass, id);
	}
}