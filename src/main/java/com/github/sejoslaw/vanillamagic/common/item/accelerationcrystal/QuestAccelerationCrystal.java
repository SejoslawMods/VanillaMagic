package com.github.sejoslaw.vanillamagic.common.item.accelerationcrystal;

import com.github.sejoslaw.vanillamagic.api.event.EventAccelerationCrystal;
import com.github.sejoslaw.vanillamagic.core.VMConfig;
import com.github.sejoslaw.vanillamagic.common.quest.Quest;
import com.github.sejoslaw.vanillamagic.common.util.EventUtil;
import com.github.sejoslaw.vanillamagic.core.VMItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.texture.ITickable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Random;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestAccelerationCrystal extends Quest {
	@SubscribeEvent
	public void rightClickBlock(RightClickBlock event) {
		PlayerEntity player = event.getPlayer();
		World world = player.world;
		BlockPos clickedPos = event.getPos();
		BlockState clickedState = world.getBlockState(clickedPos);
		Block clickedBlock = clickedState.getBlock();
		ItemStack rightHand = player.getHeldItemMainhand();

		if (world.isRemote || world.isAirBlock(clickedPos) || !VMItems.isCustomItem(rightHand, VMItems.ACCELERATION_CRYSTAL)) {
			return;
		}

		if (!hasQuest(player)) {
			addStat(player);
		}

		if (!hasQuest(player)) {
			return;
		}

		TileEntity tile = world.getTileEntity(clickedPos);
		Random rand = new Random();

		for (int i = 0; i < VMConfig.ACCELERATION_CRYSTAL_UPDATE_TICKS.get(); i++) {
			if (tile == null) {
				if (!EventUtil.postEvent(new EventAccelerationCrystal.TickBlock(VMItems.ACCELERATION_CRYSTAL, world, clickedPos, player))) {
					clickedBlock.tick(clickedState, (ServerWorld) world, clickedPos, rand);
				}
			} else if (tile instanceof ITickable) {
				if (!EventUtil.postEvent(new EventAccelerationCrystal.TickTileEntity(VMItems.ACCELERATION_CRYSTAL, world, clickedPos, player, tile))) {
					((ITickable) tile).tick();
				}
			}
		}
	}
}