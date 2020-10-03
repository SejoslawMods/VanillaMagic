package com.github.sejoslaw.vanillamagic2.common.quests.types.items;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.items.VMItemMotherNatureCrystal;
import com.github.sejoslaw.vanillamagic2.common.registries.ItemRegistry;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestMotherNatureCrystal extends QuestVMItem<VMItemMotherNatureCrystal> {
    public VMItemMotherNatureCrystal getVMItem() {
        return (VMItemMotherNatureCrystal) ItemRegistry.MOTHER_NATURE_CRYSTAL;
    }

    public void fillTooltip(List<ITextComponent> lines) {
        super.fillTooltip(lines);

        TextUtils.addLine(lines, "quest.tooltip.range", String.valueOf(VMForgeConfig.MOTHER_NATURE_CRYSTAL_RANGE.get()));
    }
}
