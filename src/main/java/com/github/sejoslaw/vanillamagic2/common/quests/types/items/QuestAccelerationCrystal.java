package com.github.sejoslaw.vanillamagic2.common.quests.types.items;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.items.VMItemAccelerationCrystal;
import com.github.sejoslaw.vanillamagic2.common.registries.ItemRegistry;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestAccelerationCrystal extends QuestVMItem<VMItemAccelerationCrystal> {
    public VMItemAccelerationCrystal getVMItem() {
        return (VMItemAccelerationCrystal) ItemRegistry.ACCELERATION_CRYSTAL;
    }

    public void fillTooltip(List<ITextComponent> lines) {
        super.fillTooltip(lines);

        TextUtils.addLine(lines, "quest.tooltip.ticks", String.valueOf(VMForgeConfig.ACCELERATION_CRYSTAL_UPDATE_TICKS.get()));
    }
}
