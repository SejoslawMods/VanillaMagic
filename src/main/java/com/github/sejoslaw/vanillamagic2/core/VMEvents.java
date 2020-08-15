package com.github.sejoslaw.vanillamagic2.core;

import com.github.sejoslaw.vanillamagic2.common.handlers.clients.OpenQuestBookHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.loading.FMLEnvironment;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class VMEvents {
    public static void initialize() {
        registerGlobalEvents();

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
    }

    private static void registerClientSpecificEvents() {
        register(new OpenQuestBookHandler());
    }

    private static void registerDedicatedServerSpecificEvents() {
    }
}
