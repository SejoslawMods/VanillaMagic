package com.github.sejoslaw.vanillamagic2.common.quests.types;

import com.github.sejoslaw.vanillamagic2.common.json.IJsonService;
import com.github.sejoslaw.vanillamagic2.common.quests.Quest;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestSummonHorde extends Quest {
    public int distanceToPlayer;

    public void readData(IJsonService jsonService) {
        super.readData(jsonService);

        this.distanceToPlayer = jsonService.getInt("distanceToPlayer");
    }
}
