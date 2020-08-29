package com.github.sejoslaw.vanillamagic2.common.quests.types.items;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.items.VMItemMotherNatureCrystal;
import com.github.sejoslaw.vanillamagic2.common.registries.ItemRegistry;

import java.util.Collection;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestMotherNatureCrystal extends QuestVMItem<VMItemMotherNatureCrystal> {
    public VMItemMotherNatureCrystal getVMItem() {
        return (VMItemMotherNatureCrystal) ItemRegistry.MOTHER_NATURE_CRYSTAL;
    }

    public void fillTooltip(Collection<String> lines) {
        super.fillTooltip(lines);

        this.addLine(lines, "quest.tooltip.range", String.valueOf(VMForgeConfig.MOTHER_NATURE_CRYSTAL_RANGE.get()));
    }
}
