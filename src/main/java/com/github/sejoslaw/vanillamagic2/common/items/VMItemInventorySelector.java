package com.github.sejoslaw.vanillamagic2.common.items;

import com.github.sejoslaw.vanillamagic2.common.utils.NbtUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VMItemInventorySelector extends VMItem {
    public Item getBaseItem() {
        return Items.BLAZE_ROD;
    }

    public List<ItemStack> getIngredients() {
        List<ItemStack> ingredients = super.getIngredients();
        ingredients.add(new ItemStack(Items.CHEST));
        return ingredients;
    }

    public ItemStack getStack() {
        ItemStack stack = super.getStack();
        clearPosition(stack);
        return stack;
    }

    public static void clearPosition(ItemStack stack) {
        setPosition(stack, 0, Integer.MIN_VALUE);
    }

    public static void setPosition(ItemStack stack, long position, int dimension) {
        stack.getOrCreateTag().putLong(NbtUtils.NBT_POSITION, position);
        stack.getOrCreateTag().putInt(NbtUtils.NBT_DIMENSION, dimension);
    }
}
