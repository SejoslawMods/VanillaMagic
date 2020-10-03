package com.github.sejoslaw.vanillamagic2.common.quests.types.items;

import com.github.sejoslaw.vanillamagic2.common.items.VMItemEvokerCrystal;
import com.github.sejoslaw.vanillamagic2.common.registries.ItemRegistry;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestEvokerCrystal extends QuestVMItem<VMItemEvokerCrystal> {
    public VMItemEvokerCrystal getVMItem() {
        return (VMItemEvokerCrystal) ItemRegistry.EVOKER_CRYSTAL;
    }

    public void fillTooltip(List<ITextComponent> lines) {
        super.fillTooltip(lines);

        TextUtils.addLine(lines, "quest.tooltip.changeSpell", TextUtils.getFormattedText("quest.evokerCrystal.desc.changeSpell"));
        TextUtils.addLine(lines, "quest.tooltip.castSpell", TextUtils.getFormattedText("quest.evokerCrystal.desc.castSpell"));
    }
}
