package com.github.sejoslaw.vanillamagic.common.quest;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestCraft extends Quest {
    @SubscribeEvent
    public void onCrafting(PlayerEvent.ItemCraftedEvent event) {
        PlayerEntity player = event.getPlayer();

        if (hasQuest(player)) {
            return;
        }

        Item item = event.getCrafting().getItem();

        if (item.equals(this.getIcon().getItem())) {
            checkQuestProgress(player);
        }
    }
}