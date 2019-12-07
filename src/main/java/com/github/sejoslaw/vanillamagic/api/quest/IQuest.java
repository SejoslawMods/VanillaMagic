package com.github.sejoslaw.vanillamagic.api.quest;

import com.google.gson.JsonObject;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import com.github.sejoslaw.vanillamagic.api.util.Point;

/**
 * This is the base of the Quest System.<br>
 * Each Quest must implements this interface to be registered from quests.json
 * file.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public interface IQuest {
    /**
     * This method is used to read data from the advancements.json<br>
     * It will create an instance of this Quest and add it to QuestList.
     */
    void readData(JsonObject jo);

    /**
     * @return Returns TRUE if the given Player can get this Quest.
     */
    boolean canPlayerGetQuest(PlayerEntity player);

    /**
     * @return Returns TRUE if this Quest needs any additional Quest to be completed
     * before this.
     */
    boolean hasAdditionalQuests();

    /**
     * @return Returns TRUE if the given Player finished all
     * additional Quests.
     */
    boolean finishedAdditionalQuests(PlayerEntity player);

    /**
     * @return Returns the Quest that is required before this one. (The parent of
     * this one).
     */
    IQuest getParent();

    /**
     * @return Returns position as {@link Point} in 2D.<br>
     */
    Point getPosition();

    /**
     * @return Returns the {@link ItemStack} that will be displayed as Icon for this
     * Quest.
     */
    ItemStack getIcon();

    /**
     * @return Returns the Quest name as a human readable string.
     */
    String getQuestName();

    /**
     * @return Returns Quest description.
     */
    String getQuestDescription();

    /**
     * @return Returns the Quest unique name.
     */
    String getUniqueName();

    /**
     * @return Returns an array of Quests that must be completed before this Quest.
     */
    IQuest[] getAdditionalRequiredQuests();

    /**
     * @return Returns QuestData connected with this Quest.
     */
    QuestData getQuestData();
}