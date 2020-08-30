package com.github.sejoslaw.vanillamagic2.common.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class VMCommand {
    protected static final String QUEST = "quest";
    protected static final String GIVE = "give";
    protected static final String GIVE_ALL = "giveAll";
    protected static final String PLAYER_NAME = "playerName";
    protected static final String QUEST_UNIQUE_NAME = "questUniqueName";

    public abstract LiteralArgumentBuilder<CommandSource> build();
    public abstract int execute(CommandContext<CommandSource> ctx) throws CommandSyntaxException;
}
