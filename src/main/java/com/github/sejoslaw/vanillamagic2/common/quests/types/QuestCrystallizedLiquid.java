package com.github.sejoslaw.vanillamagic2.common.quests.types;

import com.github.sejoslaw.vanillamagic2.common.quests.Quest;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;

import java.util.Collection;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestCrystallizedLiquid extends Quest {
    public void fillTooltip(Collection<String> lines) {
        super.fillTooltip(lines);

        this.addLine(lines, "quest.tooltip.ingredients", TextUtils.getFormattedText("quest.crystallizedLiquid.desc.ingredients"));
        this.addLine(lines, "quest.tooltip.results", TextUtils.getFormattedText("quest.crystallizedLiquid.desc.results"));
        this.addLine(lines, "quest.tooltip.usage", TextUtils.getFormattedText("quest.crystallizedLiquid.desc.usage"));
    }
}
