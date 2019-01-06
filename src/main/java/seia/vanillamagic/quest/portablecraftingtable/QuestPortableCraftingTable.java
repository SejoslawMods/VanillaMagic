package seia.vanillamagic.quest.portablecraftingtable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import seia.vanillamagic.quest.Quest;
import seia.vanillamagic.util.EntityUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestPortableCraftingTable extends Quest {
	/**
	 * On right-click open Portable Crafting Table interface.
	 */
	@SubscribeEvent
	public void openCrafting(RightClickItem event) {
		EntityPlayer player = event.getEntityPlayer();

		if (!EntityUtil.hasPlayerCraftingTableInMainHand(player)) {
			return;
		}

		checkQuestProgress(player);

		if (!hasQuest(player)) {
			return;
		}

		player.displayGui(new InterfacePortableCraftingTable(player));
	}
}