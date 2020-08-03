package com.github.sejoslaw.vanillamagic2.core;

import com.github.sejoslaw.vanillamagic2.common.tileentities.*;
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
public final class VMTiles {
    public static final TileEntityType<VMTileLiquidSuppressor> LIQUID_SUPPRESSOR = null;
    public static final TileEntityType<VMTileAccelerant> ACCELERANT = null;
    public static final TileEntityType<VMTileBlockAbsorber> BLOCK_ABSORBER = null;
    public static final TileEntityType<VMTileInventoryBridge> INVENTORY_BRIDGE = null;

    @SubscribeEvent
    public static void onTileEntitiesRegistry(RegistryEvent.Register<TileEntityType<?>> event) {
        event.getRegistry().registerAll(
                buildTileEntityType(VMTileLiquidSuppressor::new, VMTileLiquidSuppressor.class.getName()),
                buildTileEntityType(VMTileAccelerant::new, VMTileAccelerant.class.getName()),
                buildTileEntityType(VMTileBlockAbsorber::new, VMTileBlockAbsorber.class.getName()),
                buildTileEntityType(VMTileInventoryBridge::new, VMTileInventoryBridge.class.getName())
        );
    }

    private static <T extends TileEntity> TileEntityType<T> buildTileEntityType(Supplier<T> factory, String registryName) {
        Type<?> type = DataFixesManager
                .getDataFixer()
                .getSchema(DataFixUtils.makeKey(SharedConstants.getVersion().getWorldVersion()))
                .getChoiceType(TypeReferences.BLOCK_ENTITY, registryName);

        return TileEntityType.Builder
                .create(factory)
                .build(type);
    }
}
