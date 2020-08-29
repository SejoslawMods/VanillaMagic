package com.github.sejoslaw.vanillamagic2.common.quests;

import com.github.sejoslaw.vanillamagic2.common.json.IJsonService;
import com.github.sejoslaw.vanillamagic2.common.registries.QuestRegistry;
import com.github.sejoslaw.vanillamagic2.common.utils.ItemStackUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class Quest {
    public Quest parent;
    public int posX, posY;
    public ItemStack iconStack;
    public String uniqueName;

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

        if (this.multiplier > 1) {
            this.addLine(lines, "quest.tooltip.multiplier", String.valueOf(this.multiplier));
        }

        if (this.level > 0) {
            this.addLine(lines, "quest.tooltip.level", String.valueOf(this.level));
        }

        if (this.oneItemSmeltCost > 0) {
            this.addLine(lines, "quest.tooltip.oneItemSmeltCost", String.valueOf(this.oneItemSmeltCost));
        }

        this.addLine(lines, "quest.tooltip.description", TextUtils.translate("quest." + this.uniqueName + ".desc").getFormattedText());
    }

    public void addLine(Collection<String> lines, String key, String value) {
        lines.add(TextUtils.translate(key).getFormattedText() + ": " + value);
    }

    /**
     * @return Formatted display name.
     */
    public String getDisplayName() {
        return TextUtils.translate("quest." + this.uniqueName).getFormattedText();
    }

    /**
     * @return Tooltip appropriate version of the given argument.
     */
    protected String getTooltip(List<ItemStack> list) {
        return list.stream().map(this::getTooltip).collect(Collectors.joining(", "));
    }

    /**
     * @return Tooltip appropriate version of the given argument.
     */
    protected String getTooltip(ItemStack stack) {
        return stack.getCount() + "x " + stack.getDisplayName().getFormattedText();
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
}
