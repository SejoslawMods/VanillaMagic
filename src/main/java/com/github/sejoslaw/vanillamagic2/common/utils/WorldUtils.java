package com.github.sejoslaw.vanillamagic2.common.utils;

import com.github.sejoslaw.vanillamagic2.common.tileentities.IVMTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.ArrayList;
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
     * @return All ItemEntities on specified position.
     */
    public static List<ItemEntity> getItems(World world, BlockPos pos) {
        AxisAlignedBB aabb = new AxisAlignedBB(
                pos.getX() - 0.5D,
                pos.getY() - 0.5D,
                pos.getZ() - 0.5D,
                pos.getX() + 0.5D,
                pos.getY() + 0.5D,
                pos.getZ() + 0.5D);
        return new ArrayList<>(world.getEntitiesWithinAABB(ItemEntity.class, aabb));
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
            world.addEntity(new ItemEntity(world, spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), stack));
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
        TileEntity tile = world.getTileEntity(pos);
        boolean isTickable = tile instanceof ITickable;

        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();

        for (int i = 0; i < ticks; i++) {
            if (isTickable) {
                ((ITickable) tile).tick();
            } else {
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
}
