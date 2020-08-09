package com.github.sejoslaw.vanillamagic2.common.quests.types;

import com.github.sejoslaw.vanillamagic2.common.json.IJsonService;
import com.github.sejoslaw.vanillamagic2.common.quests.Quest;
import com.github.sejoslaw.vanillamagic2.common.utils.ItemStackUtils;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestCraftOnAltar extends Quest {
    public List<ItemStack> ingredients;
    public List<ItemStack> results;

    public void readData(IJsonService jsonService) {
        super.readData(jsonService);

        this.ingredients = ItemStackUtils.getItemStacksFromJson(jsonService, "ingredients");
        this.results = ItemStackUtils.getItemStacksFromJson(jsonService, "results");

        if (this.iconStack == null || this.iconStack == ItemStack.EMPTY) {
            this.iconStack = this.results.get(0);
        }
    }
}
