package com.github.sejoslaw.vanillamagic.common.tileentity;

/**
 * Class which is used to register VM CustomTileEntities.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class TileEntityRegistry {
	private TileEntityRegistry() {
	}

	public static void preInit() {
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

	private static void register(Class<? extends TileEntity> tileEntityClass, String id) {
		GameRegistry.registerTileEntity(tileEntityClass, new ResourceLocation(VanillaMagic.MODID, id));
	}
}