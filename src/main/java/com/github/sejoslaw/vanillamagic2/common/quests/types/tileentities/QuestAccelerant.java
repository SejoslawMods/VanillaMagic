package com.github.sejoslaw.vanillamagic2.common.quests.types.tileentities;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.quests.types.items.QuestAccelerationCrystal;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestAccelerant extends QuestAccelerationCrystal {
    public void fillTooltip(List<ITextComponent> lines) {
        super.fillTooltip(lines);

        TextUtils.addLine(lines, "quest.tooltip.tileTicks", String.valueOf(VMForgeConfig.TILE_ACCELERANT_TICKS.get()));
        TextUtils.addLine(lines, "quest.tooltip.size", String.valueOf(VMForgeConfig.TILE_ACCELERANT_SIZE.get()));
        TextUtils.addLine(lines, "quest.tooltip.top", TextUtils.getFormattedText("quest.accelerant.desc.top"));
    }
}
