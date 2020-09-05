package com.github.sejoslaw.vanillamagic2.common.registries;

import com.github.sejoslaw.vanillamagic2.common.tileentities.VMTileAccelerant;
import com.github.sejoslaw.vanillamagic2.common.tileentities.VMTileBlockAbsorber;
import com.github.sejoslaw.vanillamagic2.common.tileentities.VMTileInventoryBridge;
import com.github.sejoslaw.vanillamagic2.common.tileentities.VMTileLiquidSuppressor;
import com.github.sejoslaw.vanillamagic2.common.tileentities.machines.VMTileMachine;
import com.github.sejoslaw.vanillamagic2.core.VanillaMagic;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class TileEntityRegistry {
    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, VanillaMagic.MODID);

    public static final RegistryObject<TileEntityType<VMTileLiquidSuppressor>> LIQUID_SUPPRESSOR = TILE_ENTITIES.register(VMTileLiquidSuppressor.class.getName().toLowerCase(), () -> buildTileEntityType(VMTileLiquidSuppressor::new, VMTileLiquidSuppressor.getValidBlocks()));
    public static final RegistryObject<TileEntityType<VMTileAccelerant>> ACCELERANT = TILE_ENTITIES.register(VMTileAccelerant.class.getName().toLowerCase(), () -> buildTileEntityType(VMTileAccelerant::new, Blocks.CAULDRON));
    public static final RegistryObject<TileEntityType<VMTileBlockAbsorber>> BLOCK_ABSORBER = TILE_ENTITIES.register(VMTileBlockAbsorber.class.getName().toLowerCase(), () -> buildTileEntityType(VMTileBlockAbsorber::new, Blocks.AIR));
    public static final RegistryObject<TileEntityType<VMTileInventoryBridge>> INVENTORY_BRIDGE = TILE_ENTITIES.register(VMTileInventoryBridge.class.getName().toLowerCase(), () -> buildTileEntityType(VMTileInventoryBridge::new, VMTileInventoryBridge.getValidBlocks()));
    public static final RegistryObject<TileEntityType<VMTileMachine>> MACHINE = TILE_ENTITIES.register(VMTileMachine.class.getName().toLowerCase(), () -> buildTileEntityType(VMTileMachine::new, Blocks.CAULDRON));

    public static void initialize() {
        TILE_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    private static <T extends TileEntity> TileEntityType<T> buildTileEntityType(Supplier<T> factory, Block... validBlocks) {
        return TileEntityType.Builder
                .create(factory, validBlocks)
                .build(null);
    }
}
