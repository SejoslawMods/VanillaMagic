package com.github.sejoslaw.vanillamagic2.common.commands;

import com.github.sejoslaw.vanillamagic2.common.quests.Quest;
import com.github.sejoslaw.vanillamagic2.common.registries.PlayerQuestProgressRegistry;
import com.github.sejoslaw.vanillamagic2.common.utils.PlayerQuestProgressUtils;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class GiveSingleQuestCommand extends VMCommand {
    public LiteralArgumentBuilder<CommandSource> build() {
        return Commands
                .literal(QUEST)
                .then(Commands
                        .literal(GIVE)
                        .then(Commands
                                .argument(PLAYER_NAME, EntityArgument.player())
                                .then(Commands
                                        .argument(QUEST_UNIQUE_NAME, QuestArgumentType.get())
                                        .executes(this::execute))));
    }

    public int execute(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        ServerPlayerEntity player = EntityArgument.getPlayer(ctx, PLAYER_NAME);
        Quest quest = QuestArgumentType.getQuest(ctx, QUEST_UNIQUE_NAME);
        giveQuestToPlayer(player, quest);

        while (!PlayerQuestProgressRegistry.hasPlayerGotQuest(player, quest.parent.uniqueName)) {
            giveQuestToPlayer(player, quest.parent);
            quest = quest.parent;
        }

        return 0;
    }

    public static void giveQuestToPlayer(PlayerEntity player, Quest quest) {
        if (!PlayerQuestProgressRegistry.hasPlayerGotQuest(player, quest.uniqueName)) {
            PlayerQuestProgressUtils.givePlayerQuest(player, quest);
        }
    }
}
