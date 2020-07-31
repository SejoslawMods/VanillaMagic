package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.Quest;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class EventCallerUpgradable<TQuest extends Quest> extends EventCaller<TQuest> {
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
                            baseItems[0] = items[0].stream().filter(entity -> isBase(entity.getItem())).collect(Collectors.toList());
                            ingredients[0] = items[0].stream().filter(entity -> isIngredient(entity.getItem())).collect(Collectors.toList());

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

    /**
     * @return True if the specified ItemStack is a known base item.
     */
    protected abstract boolean isBase(ItemStack stack);

    /**
     * @return True if the specified ItemStack is a known ingredient.
     */
    protected abstract boolean isIngredient(ItemStack stack);

    /**
     * @return List with crafting results. Removes non-base items and non-ingredients from the respective lists.
     */
    protected abstract List<ItemStack> getResults(List<ItemEntity> baseItems, List<ItemEntity> ingredients);
}
