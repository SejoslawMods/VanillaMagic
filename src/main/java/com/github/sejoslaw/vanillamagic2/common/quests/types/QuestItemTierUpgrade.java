package com.github.sejoslaw.vanillamagic2.common.quests.types;

import com.github.sejoslaw.vanillamagic2.common.quests.Quest;
import com.github.sejoslaw.vanillamagic2.common.registries.ItemTierRegistry;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;

import java.util.Collection;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestItemTierUpgrade extends Quest {
    public void fillTooltip(Collection<String> lines) {
        super.fillTooltip(lines);

        TextUtils.addLine(lines, "", "");
        TextUtils.addLine(lines, "quest.tooltip.crafting", "");
        String toolText = TextUtils.getFormattedText("quest.tooltip.tool");

        ItemTierRegistry.getTiers().forEach((tier, stacks) -> {
            String currentTierText = ItemTierRegistry.getTierType(tier);
            String nextTierText = ItemTierRegistry.getTierType(tier + 1);

            ItemTierRegistry.getIngredients(tier + 1).forEach(stack -> {
                String message =
                        TextUtils.firstLetterToUpper(currentTierText) + " " + toolText + " + " +
                        this.getTooltip(stack) + " = " +
                        TextUtils.firstLetterToUpper(nextTierText) + " " + toolText;
                TextUtils.addLine(lines, "", message);
            });
        });
    }
}
