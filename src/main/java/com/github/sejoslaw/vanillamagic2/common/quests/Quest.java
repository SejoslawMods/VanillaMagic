package com.github.sejoslaw.vanillamagic2.common.quests;

import com.github.sejoslaw.vanillamagic2.common.json.IJsonService;
import com.github.sejoslaw.vanillamagic2.common.registries.QuestRegistry;
import com.github.sejoslaw.vanillamagic2.common.utils.ItemStackUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import net.minecraft.item.ItemStack;

import java.util.Collection;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class Quest {
    // Core fields
    public Quest parent;
    public int posX, posY;
    public ItemStack iconStack;
    public String uniqueName;

    // Fields used by classes inherited from Quest
    public int altarTier;
    public ItemStack rightHandStack;
    public ItemStack leftHandStack;
    public int multiplier;
    public int level;
    public int oneItemSmeltCost;

    public void readData(IJsonService jsonService) {
        this.parent = QuestRegistry.getQuest(jsonService.getString("parent"));
        this.iconStack = ItemStackUtils.getItemStackFromJson(jsonService.getItemStack("icon"));
        this.uniqueName = jsonService.getString("uniqueName");
        this.posX = jsonService.getInt("posX");
        this.posY = jsonService.getInt("posY");

        this.tryReadCustomFields(jsonService);
    }

    /**
     * Fields used by classes which inherits from Quest class.
     */
    private void tryReadCustomFields(IJsonService jsonService) {
        this.altarTier = jsonService.getInt("altarTier");
        this.rightHandStack = ItemStackUtils.getItemStackFromJson(jsonService.getItemStack("rightHandStack"));
        this.leftHandStack = ItemStackUtils.getItemStackFromJson(jsonService.getItemStack("leftHandStack"));
        this.multiplier = jsonService.getInt("multiplier");
        this.level = jsonService.getInt("level");
        this.oneItemSmeltCost = jsonService.getInt("oneItemSmeltCost");
    }

    /**
     * Adds tooltip information to given collection.
     */
    public void fillTooltip(Collection<String> lines) {
        this.addLine(lines, "quest.tooltip.uniqueName", TextUtils.translate("quest." + this.uniqueName).getFormattedText());

        if (this.parent != null) {
            this.addLine(lines, "quest.tooltip.parent", TextUtils.translate("quest." + this.parent.uniqueName).getFormattedText());
        }

        if (this.rightHandStack != null) {
            this.addLine(lines, "quest.tooltip.rightHandStack", this.rightHandStack.toString());
        }

        if (this.leftHandStack != null) {
            this.addLine(lines, "quest.tooltip.leftHandStack", this.leftHandStack.toString());
        }

        if (this.altarTier > 0) {
            this.addLine(lines, "quest.tooltip.altarTier", String.valueOf(this.altarTier));
        }

        this.addLine(lines, "quest.tooltip.description", TextUtils.translate("quest." + this.uniqueName + ".desc").getFormattedText());
    }

    public void addLine(Collection<String> lines, String key, String value) {
        lines.add(TextUtils.translate(key).getFormattedText() + ": " + value);
    }
}
