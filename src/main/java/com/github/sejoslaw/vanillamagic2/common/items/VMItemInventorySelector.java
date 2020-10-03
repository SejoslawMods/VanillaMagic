package com.github.sejoslaw.vanillamagic2.common.items;

import com.github.sejoslaw.vanillamagic2.common.utils.NbtUtils;
import net.minecraft.item.ItemStack;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VMItemInventorySelector extends VMItem {
    public ItemStack getStack() {
        ItemStack stack = super.getStack();
        clearPosition(stack);
        return stack;
    }

    public static void clearPosition(ItemStack stack) {
        setPosition(stack, 0, "");
    }

    public static void setPosition(ItemStack stack, long position, String dimension) {
        stack.getOrCreateTag().putLong(NbtUtils.NBT_POSITION, position);
        stack.getOrCreateTag().putString(NbtUtils.NBT_DIMENSION, dimension);
    }
}
