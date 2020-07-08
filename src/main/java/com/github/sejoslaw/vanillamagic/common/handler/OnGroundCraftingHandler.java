package com.github.sejoslaw.vanillamagic.common.handler;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class OnGroundCraftingHandler {
    public static class OnGroundCraftingEntry {
        public final ItemStack[] ingredients;
        public final ItemStack output;

        public OnGroundCraftingEntry(ItemStack output, ItemStack... ingredients) {
            this.output = output;
            this.ingredients = ingredients;
        }
    }

    public static final Set<OnGroundCraftingEntry> ENTRIES = new HashSet<>();

    @SubscribeEvent
    public void craftOnGround(PlayerInteractEvent.RightClickBlock event) {
        // TODO: Implement
    }

    public static void addRecipe(ItemStack output, ItemStack... ingredients) {
        ENTRIES.add(new OnGroundCraftingEntry(output, ingredients));
    }
}
