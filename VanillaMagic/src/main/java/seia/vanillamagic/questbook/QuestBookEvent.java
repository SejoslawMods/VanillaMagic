package seia.vanillamagic.questbook;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.util.EventUtil;
import seia.vanillamagic.util.ItemStackUtil;
import seia.vanillamagic.util.PlayerUtil;
import seia.vanillamagic.util.TextUtil;

/**
 * Events connected with VanillaMagic's QuestBook
 */
public class QuestBookEvent 
{
	/**
	 * Required stack in right hand.
	 */
	private ItemStack BOOK_STACK = new ItemStack(Items.BOOK);
	
	/**
	 * Open VM Questbook
	 */
	@SubscribeEvent
	public void openBook(RightClickItem event)
	{
		EntityPlayer playerWhoOpenedBook = EventUtil.getPlayerFromEvent(event);
		ItemStack rightHand = playerWhoOpenedBook.getHeldItemMainhand();
		if (playerWhoOpenedBook.isSneaking())
		{
			// Is Player holding book in right hand ?
			if (ItemStackUtil.checkItemsInHands(playerWhoOpenedBook, null, BOOK_STACK)) 
			{
				// Is book named correctly ?
				if (ItemStackUtil.stackNameEqual(rightHand, TextUtil.translateToLocal("questbook.itemName")))
				{
					// If singleplayer
					if (PlayerUtil.isSinglePlayer(playerWhoOpenedBook))
					{
						// Open GUI
						Minecraft.getMinecraft().displayGuiScreen(
								new GuiVMQuests(Minecraft.getMinecraft().currentScreen, playerWhoOpenedBook));
					}
					else if (PlayerUtil.isMultiPlayer(playerWhoOpenedBook))
					{
						// TODO: Open Questbook GUI on Multiplayer ???
					}
				}
			}
		}
	}
}