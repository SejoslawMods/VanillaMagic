package com.github.sejoslaw.vanillamagic2.common.quests.eventCallers.items;

import com.github.sejoslaw.vanillamagic2.common.items.ICustomItem;
import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.types.items.QuestCustomItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class EventCallerCustomItem<TQuest extends QuestCustomItem<? extends ICustomItem>> extends EventCaller<TQuest> {
    private final Map<List<ItemStack>, List<ItemStack>> recipes = new HashMap<>();

    @SubscribeEvent
    public void craft(PlayerInteractEvent.RightClickBlock event) {
        if (this.recipes.isEmpty()) {
            ICustomItem customItem = this.quests.get(0).getCustomItem();
            this.recipes.put(customItem.getIngredients(), Collections.singletonList(customItem.getStack()));
        }

        this.executor.craftOnAltar(event, this.recipes);
    }
}
