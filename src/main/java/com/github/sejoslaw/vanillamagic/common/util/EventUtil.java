package com.github.sejoslaw.vanillamagic.common.util;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;

/**
 * Various utilities connected with Minecraft Events. Wraps Minecraft Events.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class EventUtil {
    private EventUtil() {
    }

    /**
     * Register New Event.
     */
    public static void registerEvent(Object o) {
        MinecraftForge.EVENT_BUS.register(o);
    }

    /**
     * Unregisters specified event.
     */
    public static void unregisterEvent(Object o) {
        MinecraftForge.EVENT_BUS.unregister(o);
    }

    /**
     * @return Returns true if the Event was added.
     */
    public static boolean postEvent(Event event) {
        return MinecraftForge.EVENT_BUS.post(event);
    }
}