package com.github.sejoslaw.vanillamagic.common.quest;

import com.github.sejoslaw.vanillamagic.common.util.TextUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.github.sejoslaw.vanillamagic.api.event.EventJumper;
import com.github.sejoslaw.vanillamagic.common.util.TeleportUtil;
import com.github.sejoslaw.vanillamagic.util.EntityUtil;
import com.github.sejoslaw.vanillamagic.util.EventUtil;
import com.github.sejoslaw.vanillamagic.util.ItemStackUtil;
import com.github.sejoslaw.vanillamagic.util.NBTUtil;
import com.github.sejoslaw.vanillamagic.util.TextUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestJumper extends Quest {
	private static final ItemStack REQUIRED_RIGHT_HAND = new ItemStack(Items.COMPASS);
	private static final ItemStack REQUIRED_LEFT_HAND = new ItemStack(Items.BOOK);
	private static final ItemStack POSITION_HOLDER = new ItemStack(Items.ENCHANTED_BOOK);

	@SubscribeEvent
	public void saveBlockPosToBook(RightClickBlock event) {
		PlayerEntity player = event.getPlayerEntity();
		ItemStack leftHand = player.getHeldItemOffhand();

		if (!ItemStackUtil.checkItemsInHands(player, REQUIRED_LEFT_HAND, REQUIRED_RIGHT_HAND)
				|| (ItemStackUtil.getStackSize(leftHand) > 1) || !player.isSneaking() || !canPlayerGetQuest(player)) {
			return;
		}

		World world = event.getWorld();
		BlockPos posToSave = event.getPos().offset(event.getFace());

		if (EventUtil.postEvent(new EventJumper.SavePosition.Before(player, world, posToSave))) {
			return;
		}

		player.setHeldItem(Hand.OFF_HAND, writeDataToBook(world, posToSave));

		if (!hasQuest(player)) {
			addStat(player);
		}

		EntityUtil.addChatComponentMessageNoSpam(player,
				"Position saved: " + TextUtil.constructPositionString(world, posToSave));
		EventUtil.postEvent(new EventJumper.SavePosition.After(player, world, posToSave));
	}

	public static ItemStack writeDataToBook(DimensionType dimensionType, BlockPos pos) {
		ItemStack bookWithData = POSITION_HOLDER.copy();
		bookWithData.setDisplayName("Jumper's Book: " + TextUtil.constructPositionString(dimensionType, pos));
		bookWithData.setTagCompound(NBTUtil.setBlockPosDataToNBT(bookWithData.getTagCompound(), pos, world));
		return bookWithData;
	}

	/**
	 * In teleportation hands are switched.
	 * 
	 * @param event
	 */
	@SubscribeEvent
	public void teleportPlayer(RightClickItem event) {
		PlayerEntity player = event.getPlayerEntity();
		ItemStack rightHand = player.getHeldItemMainhand();

		if (!ItemStackUtil.checkItemsInHands(player, REQUIRED_RIGHT_HAND, POSITION_HOLDER) || !player.isSneaking()) {
			return;
		}

		CompoundNBT bookData = rightHand.getTagCompound();

		if (bookData == null) {
			return;
		}

		BlockPos teleportPos = NBTUtil.getBlockPosDataFromNBT(bookData);

		if (teleportPos == null) {
			return;
		}

		int dimId = NBTUtil.getDimensionFromNBT(bookData);

		if (EventUtil.postEvent(new EventJumper.Teleport.Before(player, event.getWorld(), teleportPos, dimId))) {
			return;
		}

		if (player.dimension == dimId) {
			TeleportUtil.teleportEntity(player, teleportPos);
		} else if (player instanceof ServerPlayerEntity) {
			TeleportUtil.changePlayerDimensionWithoutPortal((ServerPlayerEntity) player, dimId);
		}

		EntityUtil.addChatComponentMessageNoSpam(player,
				"Teleported to: " + TextUtil.constructPositionString(player.getEntityWorld(), teleportPos));
		EventUtil.postEvent(new EventJumper.Teleport.After(player, event.getWorld(), teleportPos, dimId));
	}

	@SubscribeEvent
	public void showSavedPosTooltip(ItemTooltipEvent event) {
		ItemStack jumperBookStack = event.getItemStack();
		CompoundNBT jumperBookTagCompound = jumperBookStack.getTagCompound();

		if (jumperBookTagCompound == null) {
			return;
		}

		BlockPos teleportPos = NBTUtil.getBlockPosDataFromNBT(jumperBookTagCompound);

		if (teleportPos == null) {
			return;
		}

		int dimId = NBTUtil.getDimensionFromNBT(jumperBookTagCompound);
		String info = TextUtil.constructPositionString(dimId, teleportPos);
		event.getToolTip().add("Jumper's Book: " + info);
	}
}