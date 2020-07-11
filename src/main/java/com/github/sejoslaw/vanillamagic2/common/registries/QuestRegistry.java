package com.github.sejoslaw.vanillamagic2.common.registries;

import com.github.sejoslaw.vanillamagic2.common.quests.Quest;
import com.github.sejoslaw.vanillamagic2.common.utils.ItemStackUtil;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class QuestRegistry {
    private static final class UserQuestData {
        public final String worldName;
        public final String playerName;
        public final Set<String> questUniqueNames;

        public UserQuestData(String worldName, String playerName, Set<String> questUniqueNames) {
            this.worldName = worldName;
            this.playerName = playerName;
            this.questUniqueNames = questUniqueNames;
        }
    }

    /**
     * Contains data read from User's Quest Files.
     */
    private static final Set<UserQuestData> USER_QUEST_DATAS = new HashSet<>();

    /**
     * Contains all registered Quests.
     */
    public static final Set<Quest> QUESTS = new HashSet<>();

    public static void parse(JsonObject jo) {
        Quest parent = QUESTS.stream().filter(q -> q.uniqueName.equals(jo.get("parent").getAsString())).findFirst().get();

        if (parent == null) {
            parent = new Quest(null, new Vec3d(0, 0, 0), ItemStack.EMPTY, "");
        }

        ItemStack iconStack = ItemStackUtil.getItemStackFromJSON(jo.get("icon").getAsJsonObject());
        String uniqueName = jo.get("uniqueName").getAsString();

        double posX = parent.position.x + jo.get("posX").getAsInt();
        double posY = parent.position.y + jo.get("posY").getAsInt();
        Vec3d position = new Vec3d(posX, posY, 0);

        QUESTS.add(new Quest(parent, position, iconStack, uniqueName));
    }

    public static void addQuestData(String worldName, String userName, Set<String> questUniqueNames) {
        USER_QUEST_DATAS.add(new UserQuestData(worldName, userName, questUniqueNames));
    }

    public static Set<String> getPlayerQuests(String worldName, String playerName) {
        return USER_QUEST_DATAS.stream().filter(data -> data.worldName.equals(worldName) && data.playerName.equals(playerName)).findFirst().get().questUniqueNames;
    }
}
