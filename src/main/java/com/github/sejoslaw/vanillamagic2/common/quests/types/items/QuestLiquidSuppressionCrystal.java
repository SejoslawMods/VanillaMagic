package com.github.sejoslaw.vanillamagic2.common.quests.types.items;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.items.VMItemLiquidSuppressionCrystal;
import com.github.sejoslaw.vanillamagic2.common.registries.ItemRegistry;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestLiquidSuppressionCrystal extends QuestVMItem<VMItemLiquidSuppressionCrystal> {
    public VMItemLiquidSuppressionCrystal getVMItem() {
        return (VMItemLiquidSuppressionCrystal) ItemRegistry.LIQUID_SUPPRESSION_CRYSTAL;
    }

    public void fillTooltip(List<ITextComponent> lines) {
        super.fillTooltip(lines);

        TextUtils.addLine(lines, "quest.tooltip.range", String.valueOf(VMForgeConfig.LIQUID_SUPPRESSION_CRYSTAL_RADIUS.get()));
        TextUtils.addLine(lines, "quest.tooltip.refreshRate", String.valueOf(VMForgeConfig.LIQUID_SUPPRESSION_CRYSTAL_REFRESH_RATE.get()));
    }
}
