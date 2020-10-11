package com.github.sejoslaw.vanillamagic2.common.utils;

import com.github.sejoslaw.vanillamagic2.common.tileentities.IVMTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.HopperTileEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.IServerWorldInfo;

import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class WorldUtils {
    /**
     * Cached name of the currently handling IWorld.
     */
    public static String WORLD_NAME = "";

    /**
     * Performs some logic only if given World is a ServerWorld.
     */
    public static void forServer(IWorld world, Consumer<IServerWorld> consumer) {
        if (!(world instanceof IServerWorld)) {
            return;
        }

        consumer.accept((IServerWorld)world);
    }

    /**
     * @return All ItemEntities on specified position.
     */
    public static List<ItemEntity> getItems(IWorld world, BlockPos pos) {
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
    public static List<ItemEntity> getOres(IWorld world, BlockPos pos) {
        return getItems(world, pos)
                .stream()
                .filter(entity -> entity.getItem().getItem().getRegistryName().toString().toLowerCase().contains("ore"))
                .collect(Collectors.toList());
    }

    /**
     * Spawns given List of items 1 block above Cauldron.
     */
    public static void spawnOnCauldron(IWorld world, BlockPos pos, List<ItemStack> stacks, Function<ItemStack, Integer> stackCountModifier) {
        BlockPos spawnPos = pos.offset(Direction.UP);

        stacks.forEach(stack -> {
            stack.setCount(stackCountModifier.apply(stack));
            Block.spawnAsEntity(WorldUtils.asWorld(world), spawnPos, stack);
        });
    }

    /**
     * Spawns VM TileEntity into the specified IWorld on the given BlockPos.
     */
    public static <TVMTileEntity extends IVMTileEntity> void spawnVMTile(IWorld world, BlockPos pos, TVMTileEntity tile, Consumer<TVMTileEntity> consumer) {
        if (WorldUtils.getIsRemote(world) || WorldUtils.getTickableTileEntities(world).stream().anyMatch(tileEntity -> tileEntity.getPos().equals(pos))) {
            return;
        }

        tile.initialize(world, pos);
        consumer.accept(tile);
        tile.spawn();
    }

    /**
     * Performs ticking logic for the specified position.
     */
    public static void tick(IWorld world, BlockPos pos, int ticks, Random rand) {
        if (WorldUtils.getIsRemote(world)) {
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
    public static IInventory getInventory(IWorld world, BlockPos pos) {
        return HopperTileEntity.getInventoryAtPosition(WorldUtils.asWorld(world), pos);
    }

    /**
     * @return Returns a VMTileEntity on specified position at specified IWorld; otherwise null.
     */
    public static IVMTileEntity getVMTile(IWorld world, BlockPos pos) {
        return getVMTiles(world, vmTile -> vmTile.getPos().equals(pos))
                .stream()
                .findFirst()
                .orElse(null);
    }

    /**
     * @return List will all currently registered VM TileEntities on the specified IWorld and satisfy the predicate.
     */
    public static List<IVMTileEntity> getVMTiles(IWorld world, Predicate<IVMTileEntity> check) {
        return WorldUtils.getTickableTileEntities(world)
                .stream()
                .filter(tile -> tile instanceof IVMTileEntity && check.test((IVMTileEntity) tile))
                .map(tile -> (IVMTileEntity) tile)
                .collect(Collectors.toList());
    }

    /**
     * @return Name of the currently handling IWorld.
     */
    public static String getWorldName(IWorld world) {
        if (world instanceof ServerWorld) {
            WORLD_NAME = ((IServerWorldInfo)world.getWorldInfo()).getWorldName();
        }

        return WORLD_NAME;
    }

    /**
     * Tries to put given stacks into specified inventory. Any leftover items / stacks will be spawned
     */
    public static void putStacksInInventoryAllSlots(IWorld world, IInventory outputInv, List<ItemStack> stacks, Direction direction, BlockPos spawnPos) {
        stacks.forEach(stack -> {
            if (stack.getItem() == Items.AIR || stack.getCount() <= 0) {
                return;
            }

            if (outputInv == null) {
                Block.spawnAsEntity(WorldUtils.asWorld(world), spawnPos, stack);
            } else {
                ItemStack leftStack = HopperTileEntity.putStackInInventoryAllSlots(null, outputInv, stack, direction);

                if (leftStack != ItemStack.EMPTY && leftStack.getCount() > 0) {
                    Block.spawnAsEntity(WorldUtils.asWorld(world), spawnPos, leftStack);
                }
            }
        });
    }

    /**
     * @return Unique identifier connected with the given IWorld.
     */
    public static ResourceLocation getId(IWorld world) {
        return asWorld(world).getDimensionKey().getLocation();
    }

    /**
     * @return True if the given IWorld is a server world; otherwise false.
     */
    public static boolean getIsRemote(IWorld world) {
        return asWorld(world).isRemote;
    }

    /**
     * @return Returns a List of Tickable TileEntities.
     */
    public static List<TileEntity> getTickableTileEntities(IWorld world) {
        return asWorld(world).tickableTileEntities;
    }

    /**
     * This method should be called only if Minecraft really needs pure IWorld object.
     *
     * @return IWorld in a form of a IWorld object.
     */
    public static World asWorld(IWorld world) {
        return (World)world;
    }
}
