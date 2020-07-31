package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.itemupgrades.BaseItemType;
import com.github.sejoslaw.vanillamagic2.common.itemupgrades.ItemUpgrade;
import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestItemUpgrade;
import com.github.sejoslaw.vanillamagic2.common.registries.ItemUpgradeRegistry;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerItemUpgrade extends EventCallerUpgradable<QuestItemUpgrade> {
    public EventCallerItemUpgrade() {
        ItemUpgradeRegistry.UPGRADES.forEach(handler -> handler.getItemUpgradeEventCaller().eventCaller = this);
    }

    protected boolean isBase(ItemStack stack) {
        return ItemUpgradeRegistry.isBase(stack);
    }

    protected boolean isIngredient(ItemStack stack) {
        return ItemUpgradeRegistry.isIngredient(stack);
    }

    protected List<ItemStack> getResults(List<ItemEntity> baseItems, List<ItemEntity> ingredients) {
        List<ItemStack> results = new ArrayList<>();

        for (ItemEntity baseEntity : baseItems) {
            ItemStack baseStack = baseEntity.getItem();
            BaseItemType stackItemType = ItemUpgradeRegistry.getBaseItemType(baseStack);
            List<ItemUpgrade> upgrades = ItemUpgradeRegistry.getUpgrades(stackItemType);

            for (ItemUpgrade upgrade : upgrades) {
                ItemEntity ingredient = ingredients.stream().filter(entity -> upgrade.isValidIngredient(entity.getItem())).findFirst().orElse(null);

                if (ingredient == null) {
                    continue;
                }

                baseStack.getOrCreateTag().putString(upgrade.getUniqueTag(), upgrade.getUniqueTag());
                ingredient.getItem().grow(-upgrade.getIngredient().getCount());
            }
        }

        return results;
    }
}
