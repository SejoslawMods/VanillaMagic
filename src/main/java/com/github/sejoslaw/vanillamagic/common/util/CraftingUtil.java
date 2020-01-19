package com.github.sejoslaw.vanillamagic.common.util;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;

/**
 * Various method for handling crafting since Minecraft 1.12
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class CraftingUtil {
    private CraftingUtil() {
    }

    /**
     * Converts an object array into a NonNullList of Ingredients
     */
    private static NonNullList<Ingredient> buildInput(Object... input) {
        NonNullList<Ingredient> list = NonNullList.create();

        for (Object obj : input) {
            if (obj instanceof Ingredient) {
                list.add((Ingredient) obj);
            } else {
                list.add(Ingredient.fromStacks((ItemStack) obj));
            }
        }

        return list;
    }
}