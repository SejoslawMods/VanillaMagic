package com.github.sejoslaw.vanillamagic.tileentity.inventorybridge;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.github.sejoslaw.vanillamagic.api.exception.NotInventoryException;
import com.github.sejoslaw.vanillamagic.handler.CustomTileEntityHandler;
import com.github.sejoslaw.vanillamagic.magic.wand.WandRegistry;
import com.github.sejoslaw.vanillamagic.quest.Quest;
import com.github.sejoslaw.vanillamagic.util.EntityUtil;
import com.github.sejoslaw.vanillamagic.util.ItemStackUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestInventoryBridge extends Quest {
	/**
	 * ItemStack required in left hand to create {@link TileInventoryBridge}
	 */
	public final ItemStack requiredLeftHand = new ItemStack(Blocks.STAINED_GLASS);

	@SubscribeEvent
	public void onRightClick(RightClickBlock event) {
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
		TileEntity clickedInventory = world.getTileEntity(clickedPos);

		if ((clickedInventory == null) || !(clickedInventory instanceof IInventory)) {
			return;
		}

		this.checkQuestProgress(player);

		if (!hasQuest(player)) {
			return;
		}

		TileInventoryBridge tile = new TileInventoryBridge();
		tile.init(player.world, clickedPos.offset(Direction.UP));

		try {
			tile.setPositionFromSelector(player);
		} catch (NotInventoryException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.out.println(e.position.toString());
			return;
		}

		try {
			tile.setOutputInventory(world, clickedPos);
		} catch (NotInventoryException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
			System.out.println(e.position.toString());
			return;
		}

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
	public void onBridgeDestroyed(BreakEvent event) {
		BlockPos inventoryPos = event.getPos();
		World world = event.getWorld();
		TileEntity inventoryTile = world.getTileEntity(inventoryPos);

		if (!(inventoryTile instanceof IInventory)) {
			return;
		}

		BlockPos customTilePos = inventoryPos.offset(Direction.UP);
		CustomTileEntityHandler.removeCustomTileEntityAndSendInfoToPlayer(world, customTilePos, event.getPlayer());
	}
}