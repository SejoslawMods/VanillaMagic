package seia.vanillamagic.item.liquidsuppressioncrystal;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import seia.vanillamagic.api.event.EventLiquidSuppressionCrystal;
import seia.vanillamagic.config.VMConfig;
import seia.vanillamagic.item.VanillaMagicItems;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.util.BlockUtil;
import seia.vanillamagic.util.EventUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestLiquidSuppressionCrystal extends Quest {
	/**
	 * if Player has the Crystal in inventory and walk near liquid source, the
	 * liquid should disappear for some time.
	 */
	@SubscribeEvent
	public void onItemHeld(PlayerTickEvent event) {
		EntityPlayer player = event.player;
		World world = player.world;
		ItemStack leftHand = player.getHeldItemOffhand();

		if (!VanillaMagicItems.isCustomItem(leftHand, VanillaMagicItems.LIQUID_SUPPRESSION_CRYSTAL)) {
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
	public void onCrystalUpdate(ItemStack leftHand, World world, EntityPlayer player) {
		int x = (int) player.posX;
		int y = (int) player.posY;
		int z = (int) player.posZ;

		int radius = VMConfig.LIQUID_SUPPRESSION_CRYSTAL_RADIUS;
		int refresh = 100; // how often block should be refreshed

		for (int i = -radius; i <= radius; ++i) {
			for (int j = -radius; j <= radius; ++j) {
				for (int k = -radius; k <= radius; ++k) {
					this.updateCrystal(leftHand, world, player, x, y, z, radius, refresh, i, j, k);
				}
			}
		}
	}

	private void updateCrystal(ItemStack leftHand, World world, EntityPlayer player, int x, int y, int z, int radius,
			int refresh, int i, int j, int k) {
		BlockPos blockPos = new BlockPos(x + i, y + j, z + k);
		IBlockState state = world.getBlockState(blockPos);

		if (BlockUtil.isBlockLiquid(state) && (world.getTileEntity(blockPos) == null)) {
			if (!EventUtil.postEvent(new EventLiquidSuppressionCrystal.CreateAirBlock(player, world, leftHand, blockPos,
					VanillaMagicItems.LIQUID_SUPPRESSION_CRYSTAL))) {
				TileLiquidSuppression.createAirBlock(world, blockPos, refresh);
			}
		} else {
			TileEntity tile = world.getTileEntity(blockPos);

			if ((tile instanceof TileLiquidSuppression)
					&& (!EventUtil.postEvent(new EventLiquidSuppressionCrystal.UseOnTileEntity(player, world, leftHand,
							blockPos, VanillaMagicItems.LIQUID_SUPPRESSION_CRYSTAL, tile)))) {
				((TileLiquidSuppression) tile).resetDuration(refresh);
			}
		}
	}
}