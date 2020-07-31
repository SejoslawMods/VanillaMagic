package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.Quest;
import com.github.sejoslaw.vanillamagic2.common.utils.NbtUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;
import java.util.function.Function;

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

    protected <T> void fillCrystalRecipesFromRegistry(
            Set<Map.Entry<ResourceLocation, T>> entries,
            Function<T, ItemStack> ingredient,
            Function<T, ItemStack> result,
            String displayNameKey) {
        for (Map.Entry<ResourceLocation, T> entry : entries) {
            List<ItemStack> ingredients = new ArrayList<>();
            ingredients.add(new ItemStack(Items.NETHER_STAR));
            ingredients.add(ingredient.apply(entry.getValue()));

            ItemStack stack = result.apply(entry.getValue());
            stack.getOrCreateTag().putString(NbtUtils.NBT_VM_ITEM_UNIQUE_NAME, entry.getKey().toString());
            stack.setDisplayName(TextUtils.combine(TextUtils.translate(displayNameKey), entry.getKey().getPath()));

            List<ItemStack> results = new ArrayList<>();
            results.add(stack);

            this.recipes.put(ingredients, results);
        }
    }
}
