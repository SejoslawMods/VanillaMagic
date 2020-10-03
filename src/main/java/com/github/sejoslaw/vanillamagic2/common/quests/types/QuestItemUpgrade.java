package com.github.sejoslaw.vanillamagic2.common.quests.types;

import com.github.sejoslaw.vanillamagic2.common.itemupgrades.BaseItemType;
import com.github.sejoslaw.vanillamagic2.common.quests.Quest;
import com.github.sejoslaw.vanillamagic2.common.registries.ItemUpgradeRegistry;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import net.minecraft.util.text.ITextComponent;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestItemUpgrade extends Quest {
    public void fillTooltip(List<ITextComponent> lines) {
        super.fillTooltip(lines);

        TextUtils.addLine(lines, "", "");
        TextUtils.addLine(lines, "quest.tooltip.crafting", "");

        BaseItemType.TYPES.forEach(baseItemType ->
                ItemUpgradeRegistry.getUpgrades(baseItemType).forEach(itemUpgrade -> {
                    String message =
                            TextUtils.firstLetterToUpper(baseItemType.itemType) + " + " +
                            this.getTooltip(itemUpgrade.getIngredient()) + " = " +
                            itemUpgrade.getClass().getSimpleName();
                    TextUtils.addLine(lines, "", message);
        }));
    }
}
