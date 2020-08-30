package com.github.sejoslaw.vanillamagic2.common.recipes;

import com.github.sejoslaw.vanillamagic2.common.quests.Quest;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class AltarRecipe {
    public final Quest quest;
    public final List<ItemStack> ingredients;
    public final List<ItemStack> results;

    public AltarRecipe(Quest quest, List<ItemStack> ingredients, List<ItemStack> results) {
        this.quest = quest;
        this.ingredients = ingredients;
        this.results = results;
    }
}
