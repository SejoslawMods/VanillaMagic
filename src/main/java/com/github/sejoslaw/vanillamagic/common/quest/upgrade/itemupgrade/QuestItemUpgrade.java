package com.github.sejoslaw.vanillamagic.common.quest.upgrade.itemupgrade;

import com.github.sejoslaw.vanillamagic.common.quest.QuestSpawnOnCauldron;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestItemUpgrade extends QuestSpawnOnCauldron {
    public boolean canGetUpgrade(ItemStack base) {
        return ItemUpgradeRegistry.canGetUpgrade(base);
    }

    public boolean isBaseItem(ItemEntity entityItem) {
        return this.canGetUpgrade(entityItem.getItem());
    }

    public ItemStack getResultSingle(ItemEntity base, ItemEntity ingredient) {
        return ItemUpgradeRegistry.getResult(base.getItem(), ingredient.getItem());
    }
}