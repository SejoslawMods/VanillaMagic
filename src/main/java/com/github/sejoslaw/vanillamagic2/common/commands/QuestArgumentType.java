package com.github.sejoslaw.vanillamagic2.common.commands;

import com.github.sejoslaw.vanillamagic2.common.quests.Quest;
import com.github.sejoslaw.vanillamagic2.common.registries.QuestRegistry;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class QuestArgumentType implements ArgumentType<Quest> {
    public Quest parse(StringReader reader) throws CommandSyntaxException {
        return QuestRegistry.getQuest(reader.readString());
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return ISuggestionProvider.suggest(this.getExamples(), builder);
    }

    public Collection<String> getExamples() {
        return QuestRegistry.getQuests()
                .stream()
                .map(q -> q.uniqueName)
                .collect(Collectors.toList());
    }

    public static QuestArgumentType get() {
        return new QuestArgumentType();
    }

    public static Quest getQuest(final CommandContext<CommandSource> ctx, final String questUniqueName) {
        return ctx.getArgument(questUniqueName, Quest.class);
    }
}
