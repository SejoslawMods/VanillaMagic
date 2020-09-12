package com.github.sejoslaw.vanillamagic2.common.items;

import com.github.sejoslaw.vanillamagic2.common.registries.EvokerSpellRegistry;
import com.github.sejoslaw.vanillamagic2.common.utils.NbtUtils;
import net.minecraft.item.ItemStack;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VMItemEvokerCrystal extends VMItem {
    public ItemStack getStack() {
        ItemStack stack = super.getStack();
        stack.getOrCreateTag().putInt(NbtUtils.NBT_SPELL_ID, 0);
        EvokerSpellRegistry.changeSpell(stack);
        return stack;
    }
}
