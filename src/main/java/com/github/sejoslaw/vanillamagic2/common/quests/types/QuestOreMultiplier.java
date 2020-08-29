package com.github.sejoslaw.vanillamagic2.common.quests.types;

import com.github.sejoslaw.vanillamagic2.common.quests.Quest;
import net.minecraft.block.Blocks;

import java.util.Collection;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestOreMultiplier extends Quest {
    public void fillTooltip(Collection<String> lines) {
        super.fillTooltip(lines);

        this.addLine(lines, "quest.tooltip.block", Blocks.FURNACE.getNameTextComponent().getFormattedText());
    }
}
