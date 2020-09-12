package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestItemTierUpgrade;
import com.github.sejoslaw.vanillamagic2.common.registries.ItemTierRegistry;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerItemTierUpgrade extends EventCallerUpgradable<QuestItemTierUpgrade> {
    protected boolean isBase(ItemStack stack) {
        return ItemTierRegistry.isBase(stack);
    }

    protected boolean isIngredient(ItemStack stack) {
        return ItemTierRegistry.isIngredient(stack);
    }

    protected List<ItemStack> getResults(List<ItemEntity> baseItems, List<ItemEntity> ingredients) {
        List<ItemStack> results = new ArrayList<>();
        Map<ItemEntity, Boolean> baseItemsChecked = baseItems.stream().collect(Collectors.toMap(e -> e, e -> false));

        for (Map.Entry<ItemEntity, Boolean> entry : baseItemsChecked.entrySet()) {
            ItemEntity baseEntity = entry.getKey();

            if (entry.getValue() || !baseEntity.getItem().getItem().getRegistryName().getPath().contains("_")) {
                continue;
            }

            ItemStack baseStack = baseEntity.getItem();
            int tier = ItemTierRegistry.getTier(baseStack);
            tier++;
            ItemEntity ingredient = this.getIngredient(tier, ingredients);

            if (ingredient == null) {
                entry.setValue(true);
                continue;
            }

            ItemStack result = this.buildResult(tier, baseStack);
            results.add(result);
        }

        return results;
    }

    private ItemEntity getIngredient(int tier, List<ItemEntity> ingredients) {
        return ingredients.stream().filter(entity -> ItemTierRegistry.getTier(entity.getItem()) == tier).findFirst().orElse(null);
    }

    private ItemStack buildResult(int tier, ItemStack baseStack) {
        String itemKind = baseStack.getItem().getRegistryName().getPath().split("_")[1].toLowerCase();
        String tierType = ItemTierRegistry.getTierType(tier);
        String newPath = tierType + "_" + itemKind;

        Item item = ForgeRegistries.ITEMS.getEntries()
                .stream()
                .filter(entry -> entry.getKey().toString().toLowerCase().split(":")[1].equals(newPath))
                .findFirst()
                .get()
                .getValue();

        return new ItemStack(item);
    }
}
