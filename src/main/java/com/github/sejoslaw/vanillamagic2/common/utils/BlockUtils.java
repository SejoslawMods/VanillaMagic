package com.github.sejoslaw.vanillamagic2.common.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPlayerDiggingPacket;
import net.minecraft.network.play.server.SChangeBlockPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.ForgeHooks;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class BlockUtils {
    /**
     * Breaks extra block on IWorld. Each broken block affects the ItemStack durability.
     */
    public static void breakBlock(ItemStack stack, IWorld world, PlayerEntity player, BlockPos pos) {
        if (world.isAirBlock(pos)) {
            return;
        }

        BlockState state = world.getBlockState(pos);
        Block block = state.getBlock();
        float strength = block.getExplosionResistance();

        if (!ForgeHooks.canHarvestBlock(state, player, world, pos) || strength > 10f) {
            return;
        }

        if (player.isCreative()) {
            block.onBlockHarvested(WorldUtils.asWorld(world), pos, state, player);

            if (block.removedByPlayer(state, WorldUtils.asWorld(world), pos, player, false, Fluids.EMPTY.getDefaultState())) {
                block.onPlayerDestroy(world, pos, state);
            }

            if (!WorldUtils.getIsRemote(world)) {
                ((ServerPlayerEntity) player).connection.sendPacket(new SChangeBlockPacket(world, pos));
            }

            return;
        }

        stack.onBlockDestroyed(WorldUtils.asWorld(world), state, pos, player);

        if (!WorldUtils.getIsRemote(world)) {
            TileEntity tileEntity = world.getTileEntity(pos);

            if (block.removedByPlayer(state, WorldUtils.asWorld(world), pos, player, true, Fluids.EMPTY.getDefaultState())) {
                block.onPlayerDestroy(world, pos, state);
                block.harvestBlock(WorldUtils.asWorld(world), player, pos, state, tileEntity, stack);
            }

            ServerPlayerEntity mpPlayer = (ServerPlayerEntity) player;
            mpPlayer.connection.sendPacket(new SChangeBlockPacket(world, pos));
        } else {
            WorldUtils.asWorld(world).playBroadcastSound(2001, pos, Block.getStateId(state));

            if (block.removedByPlayer(state, WorldUtils.asWorld(world), pos, player, true, Fluids.EMPTY.getDefaultState())) {
                block.onPlayerDestroy(world, pos, state);
            }

            stack.onBlockDestroyed(WorldUtils.asWorld(world), state, pos, player);

            Minecraft.getInstance().getConnection().sendPacket(new CPlayerDiggingPacket(CPlayerDiggingPacket.Action.STOP_DESTROY_BLOCK, pos, Direction.getFacingDirections(player)[0]));
        }
    }

    /**
     * @return Number of blocks between the specified positions.
     */
    public static int distanceInLine(BlockPos pos1, BlockPos pos2) {
        if ((pos1.getX() == pos2.getX()) && (pos1.getY() == pos2.getY())) {
            return Math.max(pos1.getZ(), pos2.getZ()) - Math.min(pos1.getZ(), pos2.getZ());
        } else if ((pos1.getZ() == pos2.getZ()) && (pos1.getY() == pos2.getY())) {
            return Math.max(pos1.getX(), pos2.getX()) - Math.min(pos1.getX(), pos2.getX());
        } else {
            return Math.max(pos1.getY(), pos2.getY()) - Math.min(pos1.getY(), pos2.getY());
        }
    }

    /**
     * @return Array from given List.
     */
    public static Block[] getValidBlocks(List<Block> blocks) {
        Block[] blocksArray = new Block[blocks.size()];

        for (int i = 0; i < blocksArray.length; ++i) {
            blocksArray[i] = blocks.get(i);
        }

        return blocksArray;
    }

    /**
     * @return Block version of the specified Item.
     */
    public static Block getBlockFromItem(Item item) {
        return Block.getBlockFromItem(item);
    }
}
