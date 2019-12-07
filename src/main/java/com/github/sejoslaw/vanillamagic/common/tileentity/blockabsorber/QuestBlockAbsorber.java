package com.github.sejoslaw.vanillamagic.tileentity.blockabsorber;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.IHopper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.github.sejoslaw.vanillamagic.handler.CustomTileEntityHandler;
import com.github.sejoslaw.vanillamagic.magic.wand.WandRegistry;
import com.github.sejoslaw.vanillamagic.quest.Quest;
import com.github.sejoslaw.vanillamagic.util.EntityUtil;
import com.github.sejoslaw.vanillamagic.util.ItemStackUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestBlockAbsorber extends Quest {
	/**
	 * ItemStack required in left hand to create {@link TileBlockAbsorber}
	 */
	public final ItemStack requiredLeftHand = new ItemStack(Blocks.GLASS);

	@SubscribeEvent
	public void onRightClickHopper(RightClickBlock event) {
		PlayerEntity player = event.getPlayerEntity();
		ItemStack rightHand = player.getHeldItemMainhand();
		ItemStack leftHand = player.getHeldItemOffhand();

		if (ItemStackUtil.isNullStack(rightHand)
				|| !WandRegistry.areWandsEqual(WandRegistry.WAND_BLAZE_ROD.getWandStack(), rightHand)
				|| ItemStackUtil.isNullStack(leftHand) || !ItemStack.areItemsEqual(leftHand, requiredLeftHand)) {
			return;
		}

		if (!player.isSneaking()) {
			return;
		}

		World world = event.getWorld();
		BlockPos clickedPos = event.getPos();
		TileEntity clickedHopper = world.getTileEntity(clickedPos);

		if ((clickedHopper == null) || !(clickedHopper instanceof IHopper)
				|| !(clickedHopper instanceof TileEntityHopper)) {
			return;
		}

		this.checkQuestProgress(player);

		if (!hasQuest(player)) {
			return;
		}

		TileBlockAbsorber tile = new TileBlockAbsorber();
		tile.init(player.world, clickedPos.offset(Direction.UP));

		if (!CustomTileEntityHandler.addCustomTileEntity(tile, player.dimension)) {
			return;
		}

		EntityUtil.addChatComponentMessageNoSpam(player, tile.getClass().getSimpleName() + " added");
		ItemStackUtil.decreaseStackSize(leftHand, 1);

		if (ItemStackUtil.getStackSize(leftHand) != 0) {
			return;
		}

		player.setItemStackToSlot(EquipmentSlotType.OFFHAND, null);
	}

	@SubscribeEvent
	public void onHopperDestroyed(BreakEvent event) {
		BlockPos hopperPos = event.getPos();
		World world = event.getWorld();
		IBlockState hopperState = world.getBlockState(hopperPos);

		if (!BlockUtil.areEqual(hopperState.getBlock(), Blocks.HOPPER)) {
			return;
		}

		BlockPos customTilePos = hopperPos.offset(Direction.UP);
		CustomTileEntityHandler.removeCustomTileEntityAndSendInfoToPlayer(world, customTilePos, event.getPlayer());
	}
}