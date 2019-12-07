package com.github.sejoslaw.vanillamagic.quest;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestCraft extends Quest {
	@SubscribeEvent
	public void onCrafting(ItemCraftedEvent event) {
		PlayerEntity player = event.player;

		if (!hasQuest(player)) {
			Item item = event.crafting.getItem();

			if (item.equals(this.getIcon().getItem())) {
				checkQuestProgress(player);
			}
		}
	}
}