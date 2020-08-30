package com.github.sejoslaw.vanillamagic2.common.handlers.servers;

import com.github.sejoslaw.vanillamagic2.common.commands.GiveAllQuestsCommand;
import com.github.sejoslaw.vanillamagic2.common.commands.GiveSingleQuestCommand;
import com.github.sejoslaw.vanillamagic2.common.commands.VMCommand;
import com.github.sejoslaw.vanillamagic2.common.handlers.EventHandler;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class ServerCommandsHandler extends EventHandler {
    private static final ServerCommandsHandler INSTANCE = new ServerCommandsHandler();
    private static final String COMMAND = "vm";

    public static void registerCommands(FMLServerStartingEvent event) {
        INSTANCE.onServerStarting(event, (server, dispatcher) -> {
            LiteralArgumentBuilder<CommandSource> builder = Commands.literal(COMMAND);

            register(builder, new GiveSingleQuestCommand());
            register(builder, new GiveAllQuestsCommand());

            dispatcher.register(builder);
        });
    }

    private static void register(LiteralArgumentBuilder<CommandSource> builder, VMCommand command) {
        builder.then(command.build().executes(command::execute));
    }
}
