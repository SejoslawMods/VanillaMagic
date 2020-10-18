package com.github.sejoslaw.vanillamagic2.common.quests.types;

import com.github.sejoslaw.vanillamagic2.common.json.IJsonService;
import com.github.sejoslaw.vanillamagic2.common.quests.Quest;
import com.github.sejoslaw.vanillamagic2.common.utils.ItemStackUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

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

    public void fillTooltip(List<ITextComponent> lines) {
        super.fillTooltip(lines);

        TextUtils.addLine(lines, "quest.tooltip.ingredients", this.getTooltip(this.ingredients));
        TextUtils.addLine(lines, "quest.tooltip.results", this.getTooltip(this.results));
    }

    public String getDisplayName() {
        return TextUtils.getFormattedText("quest.craftOnAltar.prefix") + " " + TextUtils.getFormattedText(this.results.get(0).getTranslationKey());
    }

    public String getDescription() {
        return TextUtils.getFormattedText("quest.craftOnAltar.desc");
    }
}
