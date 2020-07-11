package com.github.sejoslaw.vanillamagic.common.item.inventoryselector;

import com.github.sejoslaw.vanillamagic.api.util.TextUtil;
import com.github.sejoslaw.vanillamagic.common.inventory.InventoryHelper;
import com.github.sejoslaw.vanillamagic.common.util.EntityUtil;
import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import com.github.sejoslaw.vanillamagic.common.util.NBTUtil;
import com.github.sejoslaw.vanillamagic.core.VMItems;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Class which describes the Inventory Selector behavior.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class InventorySelector {
	int clicks = 0;

	/**
	 * On right-click select new inventory position to be saved.
	 */
	@SubscribeEvent
	public void selectInventory(PlayerInteractEvent.RightClickBlock event) {
		clicks++;

		if (clicks == 1) {
			clicks++;
		} else {
			clicks = 0;
			return;
		}

		World world = event.getWorld();

		if (world.isRemote) {
			return;
		}

		PlayerEntity player = event.getPlayer();
		ItemStack rightHand = player.getHeldItemMainhand();

		if (ItemStackUtil.isNullStack(rightHand)) {
			return;
		}

		if (!VMItems.isCustomItem(rightHand, VMItems.INVENTORY_SELECTOR)) {
			return;
		}

		BlockPos clickedPos = event.getPos();

		if (!InventoryHelper.isInventory(world, clickedPos)) {
			EntityUtil.addChatComponentMessage("Clicked block is not an Inventory");
			return;
		}

		CompoundNBT rightHandTagOld = rightHand.getTag();
		CompoundNBT rightHandTagNew = NBTUtil.setBlockPosDataToNBT(rightHandTagOld, clickedPos, world.getDimension().getType());
		rightHand.setTag(rightHandTagNew);

		EntityUtil.addChatComponentMessage("Registered Inventory at: " + TextUtil.constructPositionString(world.getDimension().getType(), clickedPos));
	}

	/**
	 * Show currently saved position.
	 */
	@SubscribeEvent
	public void showSavedPosition(PlayerInteractEvent.RightClickItem event) {
		World world = event.getWorld();

		if (!world.isRemote) {
			return;
		}

		PlayerEntity player = event.getPlayer();
		ItemStack rightHand = player.getHeldItemMainhand();

		if (ItemStackUtil.isNullStack(rightHand)) {
			return;
		}

		if (!VMItems.isCustomItem(rightHand, VMItems.INVENTORY_SELECTOR)) {
			return;
		}

		CompoundNBT rightHandTag = rightHand.getTag();

		if (player.isSneaking()) {
			if (!rightHandTag.hasUniqueId(NBTUtil.NBT_POSX)) {
				return;
			}

			EntityUtil.addChatComponentMessage("Cleared position.");
			rightHandTag.remove(NBTUtil.NBT_POSX);
			rightHandTag.remove(NBTUtil.NBT_POSY);
			rightHandTag.remove(NBTUtil.NBT_POSZ);
		} else {
			BlockPos savedPos = NBTUtil.getBlockPosDataFromNBT(rightHandTag);

			if (savedPos == null) {
				EntityUtil.addChatComponentMessage("No saved position.");
			} else {
				EntityUtil.addChatComponentMessage("Saved position: " + TextUtil.constructPositionString(world.getDimension().getType(), savedPos));
			}
		}
	}

	/**
	 * Show Inventory Selector tooltip.
	 */
	@SubscribeEvent
	public void showInventorySelectorTooltip(ItemTooltipEvent event) {
		ItemStack inventorySelector = event.getItemStack();

		if (!VMItems.isCustomItem(inventorySelector, VMItems.INVENTORY_SELECTOR)) {
			return;
		}

		CompoundNBT selectorNBT = inventorySelector.getTag();
		int dimId = selectorNBT.getInt(NBTUtil.NBT_DIMENSION);
		BlockPos selectorPos = NBTUtil.getBlockPosDataFromNBT(selectorNBT);
		DimensionType dimType = DimensionType.getById(dimId);
		event.getToolTip().add(new StringTextComponent("Saved Position: " + TextUtil.constructPositionString(dimType, selectorPos)));
	}
}
