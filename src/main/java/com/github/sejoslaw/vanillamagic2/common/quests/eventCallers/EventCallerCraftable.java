package com.github.sejoslaw.vanillamagic2.common.quests.eventCallers;

import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.Quest;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class EventCallerCraftable<TQuest extends Quest> extends EventCaller<TQuest> {
    protected final Map<List<ItemStack>, List<ItemStack>> recipes = new HashMap<>();

    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        if (this.recipes.isEmpty()) {
            this.fillRecipes();
        }

        this.executor.craftOnAltar(event, this.recipes);
    }

    /**
     * Executed on the first time to fill the list with available recipes.
     */
    public abstract void fillRecipes();
}
