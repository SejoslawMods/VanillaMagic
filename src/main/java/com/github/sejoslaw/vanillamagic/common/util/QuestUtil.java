package com.github.sejoslaw.vanillamagic.common.util;

import com.github.sejoslaw.vanillamagic.api.quest.IQuest;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.stats.Stat;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

/**
 * Various Quest-related utilities.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class QuestUtil {
    private QuestUtil() {
    }

    /**
     * @return Returns TRUE if the Player has already finished checking Quest.
     */
    public static boolean hasQuestUnlocked(PlayerEntity player, IQuest quest) {
        if (quest == null) {
            return true;
        }

        Stat stat = quest.getQuestData();
        int statValue = 0;

        if (player instanceof ClientPlayerEntity) {
            statValue = ((ClientPlayerEntity) player).getStats().getValue(stat);
        } else if (player instanceof ServerPlayerEntity) {
            statValue = ((ServerPlayerEntity) player).getStats().getValue(stat);
        }

        return statValue > 0;
    }

    public static int countRequirementsUntilAvailable(PlayerEntity player, IQuest quest) {
        if (hasQuestUnlocked(player, quest)) {
            return 0;
        }

        int i = 0;

        for (IQuest q = quest.getParent(); q != null && !hasQuestUnlocked(player, quest); ++i) {
            q = q.getParent();
        }

        return i;
    }

    /**
     * Add specified Quest for specified Player
     */
    public static void addStat(PlayerEntity player, IQuest quest) {
        player.addStat(quest.getQuestData());

        if (player instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
            serverPlayer.getStats().sendStats(serverPlayer);

            String message = TextUtil.translateToLocal("quest.achieved") + ": " + quest.getQuestName();
            EntityUtil.addChatComponentMessageNoSpam(serverPlayer, new StringTextComponent(message));
        }
    }

    /**
     * @return Returns the StatName of the given Quest.
     */
    public static ITextComponent getStatName(IQuest quest) {
        return TextUtil.wrap(quest != null ? quest.getQuestData().getName() : "");
    }
}