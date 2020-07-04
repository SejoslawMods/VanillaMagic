package com.github.sejoslaw.vanillamagic.core;

import com.github.sejoslaw.vanillamagic.common.item.liquidsuppressioncrystal.TileLiquidSuppression;
import com.github.sejoslaw.vanillamagic.common.tileentity.blockabsorber.TileBlockAbsorber;
import com.github.sejoslaw.vanillamagic.common.tileentity.chunkloader.TileChunkLoader;
import com.github.sejoslaw.vanillamagic.common.tileentity.inventorybridge.TileInventoryBridge;
import com.github.sejoslaw.vanillamagic.common.tileentity.machine.farm.TileFarm;
import com.github.sejoslaw.vanillamagic.common.tileentity.machine.quarry.TileQuarry;
import com.github.sejoslaw.vanillamagic.common.tileentity.speedy.TileSpeedy;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.types.Type;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.datafix.DataFixesManager;
import net.minecraft.util.datafix.TypeReferences;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ObjectHolder;

import java.util.function.Supplier;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
@ObjectHolder(VanillaMagic.MODID)
public class VMTileEntities {
    public static TileEntityType<TileChunkLoader> CHUNK_LOADER = null;
    public static TileEntityType<TileInventoryBridge> INVENTORY_BRIDGE = null;
    public static TileEntityType<TileBlockAbsorber> BLOCK_ABSORBER = null;
    public static TileEntityType<TileSpeedy> SPEEDY = null;
    public static TileEntityType<TileFarm> FARM = null;
    public static TileEntityType<TileQuarry> QUARRY = null;
    public static TileEntityType<TileLiquidSuppression> LIQUID_SUPPRESSION = null;

    @SubscribeEvent
    public static void onTileEntitiesRegistry(RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().registerAll(
                buildTileEntityType(TileChunkLoader::new, TileChunkLoader.REGISTRY_NAME),
                buildTileEntityType(TileInventoryBridge::new, TileInventoryBridge.REGISTRY_NAME),
                buildTileEntityType(TileBlockAbsorber::new, TileBlockAbsorber.REGISTRY_NAME),
                buildTileEntityType(TileSpeedy::new, TileSpeedy.REGISTRY_NAME),
                buildTileEntityType(TileFarm::new, TileFarm.REGISTRY_NAME),
                buildTileEntityType(TileQuarry::new, TileQuarry.REGISTRY_NAME),
                buildTileEntityType(TileLiquidSuppression::new, TileLiquidSuppression.REGISTRY_NAME)
        );
    }

    private static <T extends TileEntity> TileEntityType<T> buildTileEntityType(Supplier<T> factory, String registryName) {
        Type<?> type = DataFixesManager.getDataFixer().getSchema(DataFixUtils.makeKey(SharedConstants.getVersion().getWorldVersion())).getChoiceType(TypeReferences.BLOCK_ENTITY, registryName);

        return TileEntityType.Builder
                .create(factory)
                .build(type);
    }
}
