package com.github.sejoslaw.vanillamagic2.common.quests.eventCallers;

import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestCraftOnAltar;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerCraftOnAltar extends EventCaller<QuestCraftOnAltar> {
    private final Map<List<ItemStack>, List<ItemStack>> recipes = new HashMap<>();

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        if (this.recipes.isEmpty()) {
            this.quests.forEach(quest -> this.recipes.put(quest.ingredients, quest.results));
        }

        this.executor.craftOnAltar(event, this.recipes);
    }
}
