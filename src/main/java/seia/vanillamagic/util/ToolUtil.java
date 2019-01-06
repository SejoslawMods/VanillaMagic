package seia.vanillamagic.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.server.SPacketBlockChange;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;

/**
 * Class which store various methods connected with tools.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class ToolUtil {
	private ToolUtil() {
	}

	public static void breakExtraBlock(ItemStack stack, World world, EntityPlayer player, BlockPos pos,
			BlockPos refPos) {
		if (world.isAirBlock(pos)) {
			return;
		}

		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		IBlockState refState = world.getBlockState(refPos);
		float refStrength = ForgeHooks.blockStrength(refState, player, world, refPos);
		float strength = ForgeHooks.blockStrength(state, player, world, pos);

		if (!ForgeHooks.canHarvestBlock(block, player, world, pos) || refStrength / strength > 10f) {
			return;
		}

		if (player.capabilities.isCreativeMode) {
			block.onBlockHarvested(world, pos, state, player);

			if (block.removedByPlayer(state, world, pos, player, false)) {
				block.onBlockDestroyedByPlayer(world, pos, state);
			}

			if (!world.isRemote) {
				((EntityPlayerMP) player).connection.sendPacket(new SPacketBlockChange(world, pos));
			}

			return;
		}

		stack.onBlockDestroyed(world, state, pos, player);

		if (!world.isRemote) {

			int xp = ForgeHooks.onBlockBreakEvent(world, ((EntityPlayerMP) player).interactionManager.getGameType(),
					(EntityPlayerMP) player, pos);

			if (xp == -1) {
				return;
			}

			TileEntity tileEntity = world.getTileEntity(pos);

			if (block.removedByPlayer(state, world, pos, player, true)) {
				block.onBlockDestroyedByPlayer(world, pos, state);
				block.harvestBlock(world, player, pos, state, tileEntity, stack);
				block.dropXpOnBlockBreak(world, pos, xp);
			}

			EntityPlayerMP mpPlayer = (EntityPlayerMP) player;
			mpPlayer.connection.sendPacket(new SPacketBlockChange(world, pos));
		} else {
			world.playBroadcastSound(2001, pos, Block.getStateId(state));

			if (block.removedByPlayer(state, world, pos, player, true)) {
				block.onBlockDestroyedByPlayer(world, pos, state);
			}

			stack.onBlockDestroyed(world, state, pos, player);

			if (ItemStackUtil.getStackSize(stack) == 0 && stack == player.getHeldItemMainhand()) {
				ForgeEventFactory.onPlayerDestroyItem(player, stack, EnumHand.MAIN_HAND);
				player.setHeldItem(EnumHand.MAIN_HAND, null);
			}

			Minecraft.getMinecraft().getConnection()
					.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos,
							Minecraft.getMinecraft().objectMouseOver.sideHit));
		}
	}
}