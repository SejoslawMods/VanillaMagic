package com.github.sejoslaw.vanillamagic.common.questbook;

import com.github.sejoslaw.vanillamagic.common.util.ItemStackUtil;
import com.github.sejoslaw.vanillamagic.common.util.TextUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Events connected with VanillaMagic's QuestBook.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventQuestBook {
	/**
	 * Required stack in right hand.
	 */
	private ItemStack BOOK_STACK = new ItemStack(Items.BOOK);

	/**
	 * Open VM Questbook
	 */
	@SubscribeEvent
	public void openBook(PlayerInteractEvent.RightClickItem event) {
		PlayerEntity playerWhoOpenedBook = event.getPlayer();
		ItemStack rightHand = playerWhoOpenedBook.getHeldItemMainhand();

		if (!playerWhoOpenedBook.isSneaking()
				|| !ItemStackUtil.checkItemsInHands(playerWhoOpenedBook, null, BOOK_STACK)
				|| !rightHand.getDisplayName().getFormattedText().equals(TextUtil.translateToLocal("questbook.itemName"))) {
			return;
		}

		if (playerWhoOpenedBook instanceof ClientPlayerEntity) {
			GuiVMQuests gui = new GuiVMQuests()
					.setParentScreen(Minecraft.getInstance().currentScreen)
					.setOpener(playerWhoOpenedBook);

			Minecraft.getInstance().displayGuiScreen(gui);
		}
	}
}