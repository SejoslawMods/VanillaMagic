package com.github.sejoslaw.vanillamagic.common.quest.upgrade.itemupgrade;

import com.github.sejoslaw.vanillamagic.api.upgrade.itemupgrade.IItemUpgrade;
import net.minecraft.item.ItemStack;

import java.util.HashSet;
import java.util.Set;

public class ItemUpgradeRegistryEntry {
    public final String toolName; // e.g. pickaxe
    public final Set<IItemUpgrade> upgrades;

    public ItemUpgradeRegistryEntry(String toolName) {
        this.toolName = toolName;
        this.upgrades = new HashSet<>();
    }

    public IItemUpgrade getUpgrade(ItemStack ingredient) {
        return this.upgrades
                .stream()
                .filter(upgrade -> upgrade.getIngredient().getItem() == ingredient.getItem() && upgrade.getIngredient().getCount() == ingredient.getCount())
                .findAny()
                .orElse(null);
    }
}
