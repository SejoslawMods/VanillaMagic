package com.github.sejoslaw.vanillamagic2.common.commands;

import com.github.sejoslaw.vanillamagic2.common.registries.QuestRegistry;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class GiveAllQuestsCommand extends VMCommand {
    public LiteralArgumentBuilder<CommandSource> build() {
        return Commands
                .literal(QUEST)
                .then(Commands
                        .literal(GIVE_ALL)
                        .then(Commands
                                .argument(PLAYER_NAME, EntityArgument.player())));
    }

    public int execute(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        ServerPlayerEntity player = EntityArgument.getPlayer(ctx, PLAYER_NAME);
        QuestRegistry.getQuests().forEach(q -> GiveSingleQuestCommand.giveQuestToPlayer(player, q));
        return 0;
    }
}
