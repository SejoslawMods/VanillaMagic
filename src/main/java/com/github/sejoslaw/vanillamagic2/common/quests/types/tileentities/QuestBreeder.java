package com.github.sejoslaw.vanillamagic2.common.quests.types.tileentities;

import com.github.sejoslaw.vanillamagic2.common.quests.Quest;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestBreeder extends Quest {
    public void fillTooltip(List<ITextComponent> lines) {
        super.fillTooltip(lines);

        TextUtils.addLine(lines, "quest.tooltip.top", TextUtils.getFormattedText("quest.desc.top"));
        TextUtils.addLine(lines, "quest.tooltip.description", TextUtils.getFormattedText("quest.desc.diamondBlock"));
    }
}
