package com.github.sejoslaw.vanillamagic.event;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChorusPlant;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.github.sejoslaw.vanillamagic.api.event.EventAutoplant;
import com.github.sejoslaw.vanillamagic.config.VMConfig;
import com.github.sejoslaw.vanillamagic.util.EventUtil;
import com.github.sejoslaw.vanillamagic.util.ItemStackUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class ActionEventAutoplantItemEntity {
	@SubscribeEvent
	public void tryToPlant(ItemExpireEvent event) {
		if (!VMConfig.ITEM_CAN_AUTOPLANT) {
			return;
		}

		ItemEntity entityItem = event.getItemEntity();
		ItemStack itemStack = entityItem.getItem();

		if (itemStack != null && !ItemStackUtil.isNullStack(itemStack)) {
			Item itemToExpire = itemStack.getItem();
			Block block = Block.getBlockFromItem(itemToExpire);
			World world = entityItem.world;
			BlockPos plantPosition = entityItem.getPosition();

			if (block.canPlaceBlockAt(world, plantPosition) && block != Blocks.AIR) {
				IBlockState plantState = null;

				if ((block instanceof IGrowable)
						&& (!EventUtil.postEvent(new EventAutoplant(entityItem, world, plantPosition)))) {
					plantState = block.getStateFromMeta(itemToExpire.getMetadata(itemStack));
				} else if ((block instanceof IPlantable)
						&& !EventUtil.postEvent(new EventAutoplant(entityItem, world, plantPosition))) {
					plantState = ((IPlantable) block).getPlant(world, plantPosition);
				} else if ((itemToExpire instanceof IPlantable)
						&& !EventUtil.postEvent(new EventAutoplant(entityItem, world, plantPosition))) {
					plantState = ((IPlantable) itemToExpire).getPlant(world, plantPosition);
				} else if (block instanceof BlockChorusPlant) {
					IBlockState endStoneState = world.getBlockState(plantPosition.offset(Direction.DOWN));
					Block endStoneBlock = endStoneState.getBlock();

					if (BlockUtil.areEqual(endStoneBlock, Blocks.END_STONE)
							&& !EventUtil.postEvent(new EventAutoplant(entityItem, world, plantPosition))) {
						plantState = Blocks.CHORUS_PLANT.getDefaultState();
					}
				} else {
					return;
				}

				world.setBlockState(plantPosition, plantState);
			} else if (itemToExpire instanceof ItemDye
					&& EnumDyeColor.byDyeDamage(itemStack.getMetadata()) == EnumDyeColor.BROWN) {
				for (Direction facing : Direction.HORIZONTALS) {
					BlockPos woodPos = plantPosition.offset(facing);
					IBlockState woodState = world.getBlockState(woodPos);
					Block woodBlock = woodState.getBlock();

					if ((woodBlock == Blocks.LOG)
							&& (woodState.getValue(BlockOldLog.VARIANT) == BlockPlanks.EnumType.JUNGLE)
							&& (world.isAirBlock(plantPosition))
							&& (!EventUtil.postEvent(new EventAutoplant(entityItem, world, plantPosition)))) {
						world.setBlockState(plantPosition,
								Blocks.COCOA.getDefaultState().withProperty(BlockCocoa.FACING, facing));
					}
				}
			}
		}
	}
}