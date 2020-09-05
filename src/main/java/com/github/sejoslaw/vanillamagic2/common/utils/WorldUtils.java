package com.github.sejoslaw.vanillamagic2.common.utils;

import com.github.sejoslaw.vanillamagic2.common.tileentities.IVMTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class WorldUtils {
    /**
     * Cached name of the currently handling World.
     */
    public static String WORLD_NAME = "";

    /**
     * @return All ItemEntities on specified position.
     */
    public static List<ItemEntity> getItems(World world, BlockPos pos) {
        AxisAlignedBB aabb = new AxisAlignedBB(
                pos.getX() - 1,
                pos.getY() - 1,
                pos.getZ() - 1,
                pos.getX() + 1,
                pos.getY() + 1,
                pos.getZ() + 1);
        return world.getEntitiesWithinAABB(ItemEntity.class, aabb, Entity::isAlive);
    }

    /**
     * @return All Ores on specified position.
     */
    public static List<ItemEntity> getOres(World world, BlockPos pos) {
        return getItems(world, pos)
                .stream()
                .filter(entity -> entity.getItem().getItem().getRegistryName().toString().toLowerCase().contains("ore"))
                .collect(Collectors.toList());
    }

    /**
     * Spawns given List of items 1 block above Cauldron.
     */
    public static void spawnOnCauldron(World world, BlockPos pos, List<ItemStack> stacks, Function<ItemStack, Integer> stackCountModifier) {
        BlockPos spawnPos = pos.offset(Direction.UP);

        stacks.forEach(stack -> {
            stack.setCount(stackCountModifier.apply(stack));
            Block.spawnAsEntity(world, spawnPos, stack);
        });
    }

    /**
     * Spawns VM TileEntity into the specified World on the given BlockPos.
     */
    public static <TVMTileEntity extends IVMTileEntity> void spawnVMTile(World world, BlockPos pos, TVMTileEntity tile, Consumer<TVMTileEntity> consumer) {
        tile.initialize(world, pos);
        consumer.accept(tile);
        tile.spawn();
    }

    /**
     * Performs ticking logic for the specified position.
     */
    public static void tick(World world, BlockPos pos, int ticks, Random rand) {
        if (world.isRemote) {
            return;
        }

        TileEntity tile = world.getTileEntity(pos);
        boolean isTickable = tile instanceof ITickableTileEntity;

        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        for (int i = 0; i < ticks; i++) {
            if (isTickable) {
                ((ITickableTileEntity) tile).tick();
            } else if (world instanceof ServerWorld) {
                block.tick(state, (ServerWorld) world, pos, rand);
            }
        }
    }

    /**
     * @return Inventory on the specified position; null otherwise.
     */
    public static IInventory getInventory(World world, BlockPos pos) {
        return HopperTileEntity.getInventoryAtPosition(world, pos);
    }

    /**
     * @return List will all currently registered VM TileEntities on the specified World.
     */
    public static List<IVMTileEntity> getVMTiles(World world) {
        return world.tickableTileEntities
                .stream()
                .filter(tile -> tile instanceof IVMTileEntity)
                .map(tile -> (IVMTileEntity) tile)
                .collect(Collectors.toList());
    }

    /**
     * @return Name of the currently handling World.
     */
    public static String getWorldName(World world) {
        if (WORLD_NAME.isEmpty()) {
            WORLD_NAME = world.getWorldInfo().getWorldName();
        }
        return WORLD_NAME;
    }
}
