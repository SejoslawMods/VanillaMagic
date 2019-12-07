package com.github.sejoslaw.vanillamagic.quest.portablecraftingtable;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.github.sejoslaw.vanillamagic.quest.Quest;
import com.github.sejoslaw.vanillamagic.util.EntityUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestPortableCraftingTable extends Quest {
	/**
	 * On right-click open Portable Crafting Table interface.
	 */
	@SubscribeEvent
	public void openCrafting(RightClickItem event) {
		PlayerEntity player = event.getPlayerEntity();

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