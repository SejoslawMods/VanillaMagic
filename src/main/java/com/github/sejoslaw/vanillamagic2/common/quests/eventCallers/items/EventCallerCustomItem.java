package com.github.sejoslaw.vanillamagic2.common.quests.eventCallers.items;

import com.github.sejoslaw.vanillamagic2.common.functions.Consumer2;
import com.github.sejoslaw.vanillamagic2.common.items.ICustomItem;
import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.types.items.QuestCustomItem;
import com.github.sejoslaw.vanillamagic2.common.utils.NbtUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class EventCallerCustomItem<TQuest extends QuestCustomItem<? extends ICustomItem>> extends EventCaller<TQuest> {
    private final Map<List<ItemStack>, List<ItemStack>> recipes = new HashMap<>();

    @SubscribeEvent
    public void craft(PlayerInteractEvent.RightClickBlock event) {
        if (this.recipes.isEmpty()) {
            ICustomItem customItem = this.getCustomItem();
            this.recipes.put(customItem.getIngredients(), Collections.singletonList(customItem.getStack()));
        }

        this.executor.craftOnAltar(event, this.recipes);
    }

    protected void useCustomItem(PlayerEntity player, Consumer2<ItemStack, ICustomItem> consumer) {
        this.executor.withHands(player, (leftHandStack, rightHandStack) -> {
            ICustomItem customItem = this.getCustomItem();
            CompoundNBT nbt = rightHandStack.getOrCreateTag();
            String key = NbtUtils.NBT_CUSTOM_ITEM_UNIQUE_NAME;

            if (nbt.contains(key) && nbt.getString(key).equals(customItem.getUniqueKey())) {
                consumer.accept(rightHandStack, customItem);
                return;
            }

            nbt = leftHandStack.getOrCreateTag();

            if (nbt.contains(key) && nbt.getString(key).equals(customItem.getUniqueKey())) {
                consumer.accept(leftHandStack, customItem);
            }
        });
    }

    protected ICustomItem getCustomItem() {
        return this.quests.get(0).getCustomItem();
    }
}
