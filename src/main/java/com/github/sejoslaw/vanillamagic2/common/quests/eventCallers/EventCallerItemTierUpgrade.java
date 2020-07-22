package com.github.sejoslaw.vanillamagic2.common.quests.eventCallers;

import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestItemTierUpgrade;
import com.github.sejoslaw.vanillamagic2.common.registries.ItemTierRegistry;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerItemTierUpgrade extends EventCaller<QuestItemTierUpgrade> {
    @SubscribeEvent
    public void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        final List<ItemEntity>[] items = new List[1];       // All items in Cauldron
        final List<ItemEntity>[] baseItems = new List[1];   // "Base" items, i.e. sword, pickaxe, axe, helmet, etc.
        final List<ItemEntity>[] ingredients = new List[1]; // Ingredients required for ANY upgrade
        final List<ItemStack>[] results = new List[1];      // Results from crafting using available se items and ingredients

        this.executor.onPlayerInteract(event,
                (player, world, pos, direction) ->
                        this.executor.clickCauldron(world, pos, () -> {
                            items[0] = WorldUtils.getItems(world, pos);
                            baseItems[0] = items[0].stream().filter(entity -> ItemTierRegistry.isBase(entity.getItem())).collect(Collectors.toList());
                            ingredients[0] = items[0].stream().filter(entity -> ItemTierRegistry.isIngredient(entity.getItem())).collect(Collectors.toList());

                            if (baseItems[0].size() <= 0 || ingredients[0].size() <= 0) {
                                return null;
                            }

                            results[0] = this.getResults(baseItems[0], ingredients[0]);

                            if (results[0].size() <= 0) {
                                return null;
                            }

                            return this.quests.get(0);
                        }),
                (player, world, pos, direction, quest) -> {
                    baseItems[0].forEach(Entity::remove);

                    ingredients[0].forEach(entity -> {
                        if (entity.getItem().getCount() <= 0) {
                            entity.remove();
                        }
                    });

                    WorldUtils.spawnOnCauldron(world, pos, results[0], ItemStack::getCount);
                });
    }

    private List<ItemStack> getResults(List<ItemEntity> baseItems, List<ItemEntity> ingredients) {
        List<ItemStack> results = new ArrayList<>();

        for (ItemEntity baseEntity : baseItems) {
            ItemStack baseStack = baseEntity.getItem();
            int tier = ItemTierRegistry.getTier(baseStack);
            tier++;
            ItemEntity ingredient = this.getIngredient(tier, ingredients);

            if (ingredient == null) {
                baseItems.remove(baseEntity);
                continue;
            }

            ingredient.getItem().grow(-1);
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
