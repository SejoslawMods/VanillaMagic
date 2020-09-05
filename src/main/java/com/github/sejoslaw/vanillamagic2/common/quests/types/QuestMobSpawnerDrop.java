package com.github.sejoslaw.vanillamagic2.common.quests.types;

import com.github.sejoslaw.vanillamagic2.common.quests.Quest;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;

import java.util.Collection;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestMobSpawnerDrop extends Quest {
    public void fillTooltip(Collection<String> lines) {
        super.fillTooltip(lines);

        TextUtils.addLine(lines, "quest.tooltip.usage", TextUtils.getFormattedText("quest.mobSpawnerDropBlock.desc.usage"));
    }
}
