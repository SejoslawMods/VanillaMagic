package com.github.sejoslaw.vanillamagic.common.event;

import com.github.sejoslaw.vanillamagic.api.event.EventAutoplant;
import com.github.sejoslaw.vanillamagic.core.VMConfig;
import com.github.sejoslaw.vanillamagic.common.util.BlockUtil;
import com.github.sejoslaw.vanillamagic.common.util.EventUtil;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import net.minecraft.block.*;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ActionEventAutoplantItemEntity {

    @SubscribeEvent
    public void tryToPlant(ItemExpireEvent event) {
        if (!VMConfig.ITEM_CAN_AUTOPLANT.get()) {
            return;
        }

        ItemEntity entityItem = event.getEntityItem();
        ItemStack itemStack = entityItem.getItem();

        if (ItemStackUtil.isNullStack(itemStack)) {
            return;
        }

        Item itemToExpire = itemStack.getItem();
        Block block = Block.getBlockFromItem(itemToExpire);
        World world = entityItem.world;
        BlockPos plantPosition = entityItem.getPosition();
        BlockState plantState = null;

        if ((block instanceof IGrowable) && (!EventUtil.postEvent(new EventAutoplant(entityItem, world, plantPosition)))) {
            plantState = block.getDefaultState();
        } else if ((block instanceof IPlantable) && !EventUtil.postEvent(new EventAutoplant(entityItem, world, plantPosition))) {
            plantState = ((IPlantable) block).getPlant(world, plantPosition);
        } else if ((itemToExpire instanceof IPlantable) && !EventUtil.postEvent(new EventAutoplant(entityItem, world, plantPosition))) {
            plantState = ((IPlantable) itemToExpire).getPlant(world, plantPosition);
        } else if (block instanceof ChorusPlantBlock) {
            BlockState endStoneState = world.getBlockState(plantPosition.offset(Direction.DOWN));
            Block endStoneBlock = endStoneState.getBlock();

            if (BlockUtil.areEqual(endStoneBlock, Blocks.END_STONE) && !EventUtil.postEvent(new EventAutoplant(entityItem, world, plantPosition))) {
                plantState = Blocks.CHORUS_PLANT.getDefaultState();
            }
        } else if (block instanceof CocoaBlock) {
            for (Direction facing : Direction.Plane.HORIZONTAL) {
                BlockPos woodPos = plantPosition.offset(facing);
                BlockState woodState = world.getBlockState(woodPos);
                Block woodBlock = woodState.getBlock();

                if ((woodBlock == Blocks.JUNGLE_LOG) && (world.isAirBlock(plantPosition)) && (!EventUtil.postEvent(new EventAutoplant(entityItem, world, plantPosition)))) {
                    world.setBlockState(plantPosition, Blocks.COCOA.getDefaultState().with(CocoaBlock.HORIZONTAL_FACING, facing));
                }
            }
        } else {
            return;
        }

        world.setBlockState(plantPosition, plantState);
    }
}