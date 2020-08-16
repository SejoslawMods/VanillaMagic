package com.github.sejoslaw.vanillamagic2.common.quests;

import com.github.sejoslaw.vanillamagic2.common.json.IJsonService;
import com.github.sejoslaw.vanillamagic2.common.registries.QuestRegistry;
import com.github.sejoslaw.vanillamagic2.common.utils.ItemStackUtils;
import net.minecraft.item.ItemStack;

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
        this.posX += this.parent != null ? this.parent.posX : 0;

        this.posY = jsonService.getInt("posY");
        this.posY += this.parent != null ? this.parent.posY : 0;

        this.tryReadCustomFields(jsonService);
    }

    /**
     * Fields used by classes which inherits from Quest class.
     *
     * @param jsonService
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
