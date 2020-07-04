package com.github.sejoslaw.vanillamagic.common.item.liquidsuppressioncrystal;

import com.github.sejoslaw.vanillamagic.api.event.EventLiquidSuppressionCrystal;
import com.github.sejoslaw.vanillamagic.common.config.VMConfig;
import com.github.sejoslaw.vanillamagic.common.quest.Quest;
import com.github.sejoslaw.vanillamagic.common.util.BlockUtil;
import com.github.sejoslaw.vanillamagic.common.util.EventUtil;
import com.github.sejoslaw.vanillamagic.core.VMItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestLiquidSuppressionCrystal extends Quest {
	/**
	 * if Player has the Crystal in inventory and walk near liquid source, the
	 * liquid should disappear for some time.
	 */
	@SubscribeEvent
	public void onItemHeld(TickEvent.PlayerTickEvent event) {
		PlayerEntity player = event.player;
		World world = player.world;
		ItemStack leftHand = player.getHeldItemOffhand();

		if (!VMItems.isCustomItem(leftHand, VMItems.LIQUID_SUPPRESSION_CRYSTAL)) {
			return;
		}

		if (!hasQuest(player)) {
			addStat(player);
		}

		if (hasQuest(player)) {
			onCrystalUpdate(leftHand, world, player);
		}
	}

	/**
	 * Once per tick. Make liquid sources disappear.
	 */
	public void onCrystalUpdate(ItemStack leftHand, World world, PlayerEntity player) {
		int x = (int) player.getPosX();
		int y = (int) player.getPosY();
		int z = (int) player.getPosZ();

		int radius = VMConfig.LIQUID_SUPPRESSION_CRYSTAL_RADIUS.get();
		int refresh = 100; // how often block should be refreshed

		for (int i = -radius; i <= radius; ++i) {
			for (int j = -radius; j <= radius; ++j) {
				for (int k = -radius; k <= radius; ++k) {
					this.updateCrystal(leftHand, world, player, x, y, z, radius, refresh, i, j, k);
				}
			}
		}
	}

	private void updateCrystal(ItemStack leftHand, World world, PlayerEntity player, int x, int y, int z, int radius,
			int refresh, int i, int j, int k) {
		BlockPos blockPos = new BlockPos(x + i, y + j, z + k);
		BlockState state = world.getBlockState(blockPos);

		if (BlockUtil.isBlockLiquid(state) && (world.getTileEntity(blockPos) == null)) {
			if (!EventUtil.postEvent(new EventLiquidSuppressionCrystal.CreateAirBlock(player, world, leftHand, blockPos,
					VMItems.LIQUID_SUPPRESSION_CRYSTAL))) {
				TileLiquidSuppression.createAirBlock(world, blockPos, refresh);
			}
		} else {
			TileEntity tile = world.getTileEntity(blockPos);

			if ((tile instanceof TileLiquidSuppression) &&
					(!EventUtil.postEvent(new EventLiquidSuppressionCrystal.UseOnTileEntity(player, world, leftHand, blockPos, VMItems.LIQUID_SUPPRESSION_CRYSTAL, tile)))) {
				((TileLiquidSuppression) tile).resetDuration(refresh);
			}
		}
	}
}
