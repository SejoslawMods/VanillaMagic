package com.github.sejoslaw.vanillamagic2.core;

import com.github.sejoslaw.vanillamagic2.common.handlers.clients.*;
import com.github.sejoslaw.vanillamagic2.common.handlers.global.AutoplantHandler;
import com.github.sejoslaw.vanillamagic2.common.handlers.servers.ServerCommandsHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class VMEvents {
    public static void initialize() {
        registerGlobalEvents();
        registerListeners();

        if (isClient()) {
            registerClientSpecificEvents();
        } else {
            registerDedicatedServerSpecificEvents();
        }
    }

    public static boolean isClient() {
        return FMLEnvironment.dist == Dist.CLIENT;
    }

    public static void register(Object obj) {
        MinecraftForge.EVENT_BUS.register(obj);
    }

    private static void registerGlobalEvents() {
        register(new AutoplantHandler());
    }

    private static void registerListeners() {
        MinecraftForge.EVENT_BUS.addListener(ServerCommandsHandler::registerCommands);
    }

    private static void registerClientSpecificEvents() {
        register(new OpenQuestGuiHandler());
        register(new ShowDeathPointHandler());
        register(new ShowHungerTooltipHandler());
        register(new ShowSaturationTooltipHandler());
        register(new ShowDurabilityTooltipHandler());
    }

    private static void registerDedicatedServerSpecificEvents() {
    }
}
