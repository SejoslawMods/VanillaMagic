package com.github.sejoslaw.vanillamagic.common.item;

import com.github.sejoslaw.vanillamagic.api.item.ICustomItem;
import net.minecraft.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class CustomItemRegistry {
    private static final Set<CustomItemRecipe> RECIPES = new HashSet<>();

    private CustomItemRegistry() {
    }

    public static void addRecipe(ICustomItem output, ItemStack... ingredients) {
        CustomItemRecipe recipe = new CustomItemRecipe(output, ingredients);
        RECIPES.add(recipe);
    }
}
