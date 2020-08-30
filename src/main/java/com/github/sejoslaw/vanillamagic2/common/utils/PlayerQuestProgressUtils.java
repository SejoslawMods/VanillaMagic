package com.github.sejoslaw.vanillamagic2.common.utils;

import com.github.sejoslaw.vanillamagic2.common.quests.Quest;
import com.github.sejoslaw.vanillamagic2.common.registries.PlayerQuestProgressRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TextFormatting;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class PlayerQuestProgressUtils {
    /**
     * Gives specified Player the Quest.
     */
    public static void givePlayerQuest(PlayerEntity player, Quest quest) {
        PlayerQuestProgressRegistry.givePlayerQuest(player, quest.uniqueName);
        onQuestCompleted(quest);
    }

    /**
     * Performs additional logic when Player received Quest.
     */
    private static void onQuestCompleted(Quest quest) {
        String questAcquiredMessage =
                TextFormatting.GREEN + TextUtils.getFormattedText("vm.message.questCompleted") +
                        TextFormatting.WHITE + " " + quest.getDisplayName();

        TextUtils.addChatMessage(questAcquiredMessage);
    }
}
