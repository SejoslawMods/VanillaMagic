package com.github.sejoslaw.vanillamagic.tileentity;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import com.github.sejoslaw.vanillamagic.core.VanillaMagic;
import com.github.sejoslaw.vanillamagic.item.liquidsuppressioncrystal.TileLiquidSuppression;
import com.github.sejoslaw.vanillamagic.tileentity.blockabsorber.TileBlockAbsorber;
import com.github.sejoslaw.vanillamagic.tileentity.chunkloader.TileChunkLoader;
import com.github.sejoslaw.vanillamagic.tileentity.inventorybridge.TileInventoryBridge;
import com.github.sejoslaw.vanillamagic.tileentity.machine.autocrafting.TileAutocrafting;
import com.github.sejoslaw.vanillamagic.tileentity.machine.farm.TileFarm;
import com.github.sejoslaw.vanillamagic.tileentity.machine.quarry.TileQuarry;
import com.github.sejoslaw.vanillamagic.tileentity.speedy.TileSpeedy;

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