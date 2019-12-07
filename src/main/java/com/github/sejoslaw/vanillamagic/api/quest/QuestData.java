package com.github.sejoslaw.vanillamagic.api.quest;

import net.minecraft.stats.IStatFormatter;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.util.registry.Registry;

/**
 * Holds data for Minecraft statistics about single Quest.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestData extends Stat {
    /**
     * Quest connected with this QuestData.
     */
    private final IQuest quest;

    /**
     * @param questUniqueId -> "vanillamagic:" + questUniqueName
     */
    public QuestData(String questUniqueId, IQuest connectedQuest) {
        super(Registry.register(Registry.STATS, questUniqueId, new StatType<>(Registry.CUSTOM_STAT)),
                null,
                IStatFormatter.DEFAULT);
        this.quest = connectedQuest;
    }

    /**
     * @return Returns Quest connected with this QuestData.
     */
    public IQuest getQuest() {
        return this.quest;
    }
}