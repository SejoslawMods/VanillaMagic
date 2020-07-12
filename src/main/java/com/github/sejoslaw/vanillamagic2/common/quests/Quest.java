package com.github.sejoslaw.vanillamagic2.common.quests;

import com.github.sejoslaw.vanillamagic2.common.registries.QuestRegistry;
import com.github.sejoslaw.vanillamagic2.common.utils.ItemStackUtil;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class Quest {
    public Quest parent;
    public Vec3d position;
    public ItemStack iconStack;
    public String uniqueName;

    public void readData(JsonObject jo) {
        parent = QuestRegistry.getQuest(jo.get("parent").getAsString());

        if (parent == null) {
            parent = getEmpty();
        }

        iconStack = ItemStackUtil.getItemStackFromJSON(jo.get("icon").getAsJsonObject());
        uniqueName = jo.get("uniqueName").getAsString();

        double posX = parent.position.x + jo.get("posX").getAsInt();
        double posY = parent.position.y + jo.get("posY").getAsInt();

        position = new Vec3d(posX, posY, 0);
    }

    public static Quest getEmpty() {
        Quest quest = new Quest();

        quest.parent = null;
        quest.uniqueName = "";
        quest.iconStack = ItemStack.EMPTY;
        quest.position = new Vec3d(0, 0, 0);

        return quest;
    }
}
