package seia.vanillamagic.questbook;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.api.quest.QuestList;
import seia.vanillamagic.util.EntityUtil;
import seia.vanillamagic.util.EventUtil;
import seia.vanillamagic.util.ItemStackUtil;
import seia.vanillamagic.util.QuestUtil;
import seia.vanillamagic.util.TextUtil;

/**
 * Events connected with VanillaMagic's QuestBook
 */
public class EventQuestBook 
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
					QuestUtil.addStat(playerWhoOpenedBook, QuestList.get(0));
					
					// If singleplayer
					if (EntityUtil.isSinglePlayer(playerWhoOpenedBook))
					{
						// Open GUI
						Minecraft.getMinecraft().displayGuiScreen(
								new GuiVMQuests(Minecraft.getMinecraft().currentScreen, playerWhoOpenedBook));
					}
					else if (EntityUtil.isMultiPlayer(playerWhoOpenedBook))
					{
						// TODO: Open Questbook GUI on Multiplayer ???
					}
				}
			}
		}
	}
}