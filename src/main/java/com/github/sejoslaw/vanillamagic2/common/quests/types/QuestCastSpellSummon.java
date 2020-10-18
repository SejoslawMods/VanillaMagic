package com.github.sejoslaw.vanillamagic2.common.quests.types;

import com.github.sejoslaw.vanillamagic2.common.spells.summon.SpellSummonEntity;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestCastSpellSummon extends QuestCastSpell {
    public String getDisplayName() {
        if (this.getSpell() == null) {
            return super.getDisplayName();
        }

        return TextUtils.getFormattedText("quest.summon.prefix") + " " + TextUtils.getFormattedText(((SpellSummonEntity)this.getSpell()).getEntityType().getTranslationKey());
    }

    public String getDescription() {
        return TextUtils.getFormattedText("quest.summon.desc");
    }
}
