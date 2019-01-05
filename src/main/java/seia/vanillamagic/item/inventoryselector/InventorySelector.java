package seia.vanillamagic.item.inventoryselector;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.inventory.InventoryHelper;
import seia.vanillamagic.item.VanillaMagicItems;
import seia.vanillamagic.util.EntityUtil;
import seia.vanillamagic.util.ItemStackUtil;
import seia.vanillamagic.util.NBTUtil;
import seia.vanillamagic.util.TextUtil;

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
	public void selectInventory(RightClickBlock event) {
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

		EntityPlayer player = event.getEntityPlayer();
		ItemStack rightHand = player.getHeldItemMainhand();

		if (ItemStackUtil.isNullStack(rightHand)) {
			return;
		}

		if (!VanillaMagicItems.isCustomItem(rightHand, VanillaMagicItems.INVENTORY_SELECTOR)) {
			return;
		}

		BlockPos clickedPos = event.getPos();

		if (!InventoryHelper.isInventory(world, clickedPos)) {
			EntityUtil.addChatComponentMessageNoSpam(player, "Clicked block is not an Inventory");
			return;
		}

		NBTTagCompound rightHandTagOld = rightHand.getTagCompound();
		NBTTagCompound rightHandTagNew = NBTUtil.setBlockPosDataToNBT(rightHandTagOld, clickedPos, world);
		rightHand.setTagCompound(rightHandTagNew);
		EntityUtil.addChatComponentMessageNoSpam(player,
				"Registered Inventory at: " + TextUtil.constructPositionString(world, clickedPos));
	}

	/**
	 * Show currently saved position.
	 */
	@SubscribeEvent
	public void showSavedPosition(RightClickItem event) {
		World world = event.getWorld();

		if (!world.isRemote) {
			return;
		}

		EntityPlayer player = event.getEntityPlayer();
		ItemStack rightHand = player.getHeldItemMainhand();

		if (ItemStackUtil.isNullStack(rightHand)) {
			return;
		}

		if (!VanillaMagicItems.isCustomItem(rightHand, VanillaMagicItems.INVENTORY_SELECTOR)) {
			return;
		}

		NBTTagCompound rightHandTag = rightHand.getTagCompound();

		if (player.isSneaking()) {
			// Clear saved position
			if (!rightHandTag.hasKey(NBTUtil.NBT_POSX)) {
				return;
			}

			EntityUtil.addChatComponentMessageNoSpam(player, "Cleared position.");
			rightHandTag.removeTag(NBTUtil.NBT_POSX);
			rightHandTag.removeTag(NBTUtil.NBT_POSY);
			rightHandTag.removeTag(NBTUtil.NBT_POSZ);
		} else {
			// Show saved position
			BlockPos savedPos = NBTUtil.getBlockPosDataFromNBT(rightHandTag);

			if (savedPos == null) {
				EntityUtil.addChatComponentMessageNoSpam(player, "No saved position.");
			} else {
				EntityUtil.addChatComponentMessageNoSpam(player,
						"Saved position: " + TextUtil.constructPositionString(world, savedPos));
			}
		}
	}

	/**
	 * Show Inventory Selector tooltip.
	 */
	@SubscribeEvent
	public void showInventorySelectorTooltip(ItemTooltipEvent event) {
		ItemStack inventorySelector = event.getItemStack();

		if (!VanillaMagicItems.isCustomItem(inventorySelector, VanillaMagicItems.INVENTORY_SELECTOR)) {
			return;
		}

		NBTTagCompound selectorNBT = inventorySelector.getTagCompound();
		int dimId = selectorNBT.getInteger(NBTUtil.NBT_DIMENSION);
		BlockPos selectorPos = NBTUtil.getBlockPosDataFromNBT(selectorNBT);
		event.getToolTip().add("Saved Position: " + TextUtil.constructPositionString(dimId, selectorPos));
	}
}