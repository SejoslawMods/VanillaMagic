package com.github.sejoslaw.vanillamagic2.common.quests.types;

import com.github.sejoslaw.vanillamagic2.common.spells.summon.SpellSummonEntity;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestCastSpellSummon extends QuestCastSpell {
    public void fillTooltip(List<ITextComponent> lines) {
        TextUtils.addLine(lines, "quest.tooltip.uniqueName", TextFormatting.YELLOW + this.getDisplayName());
        TextUtils.addLine(lines, "quest.tooltip.parent", this.parent.getDisplayName());
        TextUtils.addLine(lines, "quest.tooltip.rightHandStack", this.getTooltip(this.rightHandStack));
        TextUtils.addLine(lines, "quest.tooltip.leftHandStack", this.getTooltip(this.leftHandStack));
        TextUtils.addLine(lines, "quest.tooltip.description", TextUtils.getFormattedText("quest.summon.desc"));
    }

    public String getDisplayName() {
        if (this.getSpell() == null) {
            return super.getDisplayName();
        }

        return TextUtils.getFormattedText("quest.summon.prefix") + " " + TextUtils.getFormattedText(((SpellSummonEntity)this.getSpell()).getEntityType().getTranslationKey());
    }
}
