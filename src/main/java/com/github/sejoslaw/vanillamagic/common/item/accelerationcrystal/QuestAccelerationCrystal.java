package com.github.sejoslaw.vanillamagic.item.accelerationcrystal;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.github.sejoslaw.vanillamagic.api.event.EventAccelerationCrystal;
import com.github.sejoslaw.vanillamagic.config.VMConfig;
import com.github.sejoslaw.vanillamagic.item.VanillaMagicItems;
import com.github.sejoslaw.vanillamagic.quest.Quest;
import com.github.sejoslaw.vanillamagic.util.EventUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestAccelerationCrystal extends Quest {
	@SubscribeEvent
	public void rightClickBlock(RightClickBlock event) {
		PlayerEntity player = event.getPlayerEntity();
		World world = player.world;
		BlockPos clickedPos = event.getPos();
		IBlockState clickedState = world.getBlockState(clickedPos);
		Block clickedBlock = clickedState.getBlock();

		if (world.isRemote) {
			return;
		}

		if (!world.isAirBlock(clickedPos)) {
			ItemStack rightHand = player.getHeldItemMainhand();

			if (VanillaMagicItems.isCustomItem(rightHand, VanillaMagicItems.ACCELERATION_CRYSTAL)) {
				if (!hasQuest(player)) {
					addStat(player);
				}

				if (hasQuest(player)) {
					TileEntity tile = world.getTileEntity(clickedPos);
					Random rand = new Random();

					for (int i = 0; i < VMConfig.ACCELERATION_CRYSTAL_UPDATE_TICKS; i++) {
						if (tile == null) {
							if (!EventUtil.postEvent(new EventAccelerationCrystal.TickBlock(
									VanillaMagicItems.ACCELERATION_CRYSTAL, world, clickedPos, player))) {
								clickedBlock.updateTick(world, clickedPos, clickedState, rand);
							}
						} else if (tile instanceof ITickable) {
							if (!EventUtil.postEvent(new EventAccelerationCrystal.TickTileEntity(
									VanillaMagicItems.ACCELERATION_CRYSTAL, world, clickedPos, player, tile))) {
								((ITickable) tile).update();
							}
						}
					}
				}
			}
		}
	}
}