package com.github.sejoslaw.vanillamagic2.core;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.lifecycle.*;

import java.util.function.Consumer;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class VMEvents {
    public static void initialize() {
        addListener(VMEvents::onCommonSetup);
        addListener(VMEvents::onClientSetup);
        addListener(VMEvents::onDedicatedServerSetup);
        addListener(VMEvents::onInterModEnqueue);
        addListener(VMEvents::onInterModProcess);
    }

    private static <T extends Event> void addListener(Consumer<T> consumer) {
        MinecraftForge.EVENT_BUS.addListener(consumer);
    }

    private static void onCommonSetup(FMLCommonSetupEvent event) {
    }

    private static void onClientSetup(FMLClientSetupEvent event) {
    }

    private static void onDedicatedServerSetup(FMLDedicatedServerSetupEvent event) {
    }

    private static void onInterModEnqueue(InterModEnqueueEvent event) {
    }

    private static void onInterModProcess(InterModProcessEvent event) {
    }
}
