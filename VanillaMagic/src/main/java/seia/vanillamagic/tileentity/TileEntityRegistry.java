package seia.vanillamagic.tileentity;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;
import seia.vanillamagic.chunkloader.TileChunkLoader;
import seia.vanillamagic.item.liquidsuppressioncrystal.TileLiquidSuppression;
import seia.vanillamagic.machine.autocrafting.TileAutocrafting;
import seia.vanillamagic.machine.farm.TileFarm;
import seia.vanillamagic.machine.quarry.TileQuarry;
import seia.vanillamagic.machine.speedy.TileSpeedy;

public class TileEntityRegistry 
{
	public static final TileEntityRegistry INSTANCE = new TileEntityRegistry();
	
	public List<Class<? extends TileEntity>> registeredClasses = new ArrayList<Class<? extends TileEntity>>();
	
	private TileEntityRegistry()
	{
	}
	
	public void preInit()
	{
		// Register TileEntities
//		GameRegistry.registerTileEntity(TileQuarry.class, TileQuarry.REGISTRY_NAME);
//		GameRegistry.registerTileEntity(TileChunkLoader.class, TileChunkLoader.REGISTRY_NAME);
//		GameRegistry.registerTileEntity(TileFarm.class, TileFarm.REGISTRY_NAME);
//		GameRegistry.registerTileEntity(TileAutocrafting.class, TileAutocrafting.REGISTRY_NAME);
//		GameRegistry.registerTileEntity(TileSpeedy.class, TileSpeedy.REGISTRY_NAME);
//		GameRegistry.registerTileEntity(TileLiquidSuppression.class, TileLiquidSuppression.REGISTRY_NAME);
		register(TileQuarry.class, TileQuarry.REGISTRY_NAME);
		register(TileChunkLoader.class, TileChunkLoader.REGISTRY_NAME);
		register(TileFarm.class, TileFarm.REGISTRY_NAME);
		register(TileAutocrafting.class, TileAutocrafting.REGISTRY_NAME);
		register(TileSpeedy.class, TileSpeedy.REGISTRY_NAME);
		register(TileLiquidSuppression.class, TileLiquidSuppression.REGISTRY_NAME);
	}
	
	private void register(Class<? extends TileEntity> tileEntityClass, String id)
	{
		GameRegistry.registerTileEntity(tileEntityClass, id);
		registeredClasses.add(tileEntityClass);
	}
}