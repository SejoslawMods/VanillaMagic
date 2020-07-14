package com.github.sejoslaw.vanillamagic2.common.quests.types;

import com.github.sejoslaw.vanillamagic2.common.json.IJsonService;
import com.github.sejoslaw.vanillamagic2.common.quests.Quest;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestSmeltOnAltar extends Quest {
    public int oneItemSmeltCost;

    public void readData(IJsonService jsonService) {
        super.readData(jsonService);

        this.oneItemSmeltCost = jsonService.getInt("oneItemSmeltCost");
    }
}
