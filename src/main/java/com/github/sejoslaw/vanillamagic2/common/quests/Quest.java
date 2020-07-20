package com.github.sejoslaw.vanillamagic2.common.quests;

import com.github.sejoslaw.vanillamagic2.common.json.IJsonService;
import com.github.sejoslaw.vanillamagic2.common.registries.QuestRegistry;
import com.github.sejoslaw.vanillamagic2.common.utils.ItemStackUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class Quest {
    // Core fields
    public Quest parent;
    public Vec3d position;
    public ItemStack iconStack;
    public String uniqueName;

    // Fields used by classes inherited from Quest
    public int altarTier;
    public ItemStack rightHandStack;
    public ItemStack leftHandStack;
    public int wandTier; // TODO: Can delete ???
    public int multiplier;
    public int level;
    public int singleItemSmeltingCost;

    public void readData(IJsonService jsonService) {
        this.parent = QuestRegistry.getQuest(jsonService.getString("parent"));

        if (this.parent == null) {
            this.parent = getEmpty();
        }

        this.iconStack = ItemStackUtils.getItemStackFromJson(jsonService.getItemStack("icon"));
        this.uniqueName = jsonService.getString("uniqueName");

        double posX = this.parent.position.x + jsonService.getInt("posX");
        double posY = this.parent.position.y + jsonService.getInt("posY");

        this.position = new Vec3d(posX, posY, 0);

        this.tryReadCustomFields(jsonService);
    }

    public static Quest getEmpty() {
        Quest quest = new Quest();

        quest.parent = null;
        quest.uniqueName = "";
        quest.iconStack = ItemStack.EMPTY;
        quest.position = new Vec3d(0, 0, 0);

        return quest;
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
        this.wandTier = jsonService.getInt("wandTier");
        this.multiplier = jsonService.getInt("multiplier");
        this.level = jsonService.getInt("level");
        this.singleItemSmeltingCost = jsonService.getInt("singleItemSmeltingCost");
    }
}
