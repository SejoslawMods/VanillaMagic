package com.github.sejoslaw.vanillamagic2.common.itemupgrades;

import com.github.sejoslaw.vanillamagic2.common.functions.Action;
import com.github.sejoslaw.vanillamagic2.common.quests.eventcallers.EventCallerItemUpgrade;
import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestItemUpgrade;
import com.github.sejoslaw.vanillamagic2.common.registries.PlayerQuestProgressRegistry;
import com.github.sejoslaw.vanillamagic2.core.VMEvents;
import net.minecraft.entity.player.PlayerEntity;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ItemUpgradeEventCaller {
    public EventCallerItemUpgrade eventCaller;
    public ItemUpgrade itemUpgrade;

    public void register() {
        VMEvents.register(this);
    }

    public void execute(PlayerEntity player, Action action) {
        if (!this.itemUpgrade.containsTag(player.getHeldItemMainhand())) {
            return;
        }

        action.execute();
    }

    protected QuestItemUpgrade getQuest(PlayerEntity player) {
        QuestItemUpgrade quest = this.eventCaller.quests.get(0);
        return PlayerQuestProgressRegistry.hasPlayerGotQuest(player, quest.uniqueName) ? quest : null;
    }
}
