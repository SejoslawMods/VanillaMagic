package com.github.sejoslaw.vanillamagic2.common.items;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class CustomItemMotherNatureCrystal extends CustomItemCrystal {
    public List<ItemStack> getIngredients() {
        List<ItemStack> stacks = super.getIngredients();
        stacks.add(new ItemStack(Items.MELON, 2));
        stacks.add(new ItemStack(Items.WHEAT_SEEDS, 4));
        stacks.add(new ItemStack(Items.PUMPKIN, 2));
        return stacks;
    }
}
