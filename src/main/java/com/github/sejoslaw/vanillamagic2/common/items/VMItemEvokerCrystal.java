package com.github.sejoslaw.vanillamagic2.common.items;

import com.github.sejoslaw.vanillamagic2.common.utils.NbtUtils;
import net.minecraft.item.ItemStack;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VMItemEvokerCrystal extends VMItem {
    public ItemStack getStack() {
        ItemStack stack = super.getStack();
        stack.getOrCreateTag().putInt(NbtUtils.NBT_SPELL_ID, -1);
        return stack;
    }
}
