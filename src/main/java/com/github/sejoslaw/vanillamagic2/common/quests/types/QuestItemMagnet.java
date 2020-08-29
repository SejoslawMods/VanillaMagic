package com.github.sejoslaw.vanillamagic2.common.quests.types;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.quests.Quest;

import java.util.Collection;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestItemMagnet extends Quest {
    public final int range = VMForgeConfig.ITEM_MAGNET_RANGE.get();

    public void fillTooltip(Collection<String> lines) {
        super.fillTooltip(lines);

        this.addLine(lines, "quest.tooltip.range", String.valueOf(this.range));
    }
}
