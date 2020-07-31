package com.github.sejoslaw.vanillamagic2.common.items;

import com.github.sejoslaw.vanillamagic2.common.registries.EvokerSpellRegistry;
import com.github.sejoslaw.vanillamagic2.common.utils.NbtUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VMItemEvokerCrystal extends VMItemCrystal {
    public List<ItemStack> getIngredients() {
        List<ItemStack> stacks = super.getIngredients();
        stacks.add(new ItemStack(Items.TOTEM_OF_UNDYING));
        return stacks;
    }

    public ItemStack getStack() {
        ItemStack stack = super.getStack();
        stack.getOrCreateTag().putInt(NbtUtils.NBT_SPELL_ID, -1);
        EvokerSpellRegistry.changeSpell(stack);
        return stack;
    }
}
