package com.github.sejoslaw.vanillamagic.questbook;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.github.sejoslaw.vanillamagic.util.EntityUtil;
import com.github.sejoslaw.vanillamagic.util.EventUtil;
import com.github.sejoslaw.vanillamagic.util.ItemStackUtil;
import com.github.sejoslaw.vanillamagic.util.TextUtil;

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
	public void openBook(RightClickItem event) {
		PlayerEntity playerWhoOpenedBook = EventUtil.getPlayerFromEvent(event);
		ItemStack rightHand = playerWhoOpenedBook.getHeldItemMainhand();

		if (!playerWhoOpenedBook.isSneaking() || !ItemStackUtil.checkItemsInHands(playerWhoOpenedBook, null, BOOK_STACK)
				|| !ItemStackUtil.stackNameEqual(rightHand, TextUtil.translateToLocal("questbook.itemName"))) {
			return;
		}

		if (EntityUtil.isSinglePlayer(playerWhoOpenedBook)) {
			Minecraft.getMinecraft()
					.displayGuiScreen(new GuiVMQuests(Minecraft.getMinecraft().currentScreen, playerWhoOpenedBook));
		} else if (EntityUtil.isMultiPlayer(playerWhoOpenedBook)) {
			// TODO: Open Questbook GUI on Multiplayer ???
		}
	}
}