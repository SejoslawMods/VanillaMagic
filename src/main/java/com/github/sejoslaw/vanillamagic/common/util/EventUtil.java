package com.github.sejoslaw.vanillamagic.common.util;

import com.github.sejoslaw.vanillamagic.common.event.ActionEventAdditionalToolTips;
import com.github.sejoslaw.vanillamagic.common.event.ActionEventAutoplantItemEntity;
import com.github.sejoslaw.vanillamagic.common.event.ActionEventDeathPoint;
import com.github.sejoslaw.vanillamagic.common.handler.OnGroundCraftingHandler;
import com.github.sejoslaw.vanillamagic.common.handler.PlayerEventHandler;
import com.github.sejoslaw.vanillamagic.common.handler.WorldHandler;
import com.github.sejoslaw.vanillamagic.common.item.inventoryselector.InventorySelector;
import com.github.sejoslaw.vanillamagic.common.questbook.EventQuestBook;
import com.github.sejoslaw.vanillamagic.core.VMDebug;
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

    public static void initialize() {
        registerEvent(new EventQuestBook());
        registerEvent(new PlayerEventHandler());
        registerEvent(new InventorySelector());
        registerEvent(new VMDebug());
        registerEvent(new WorldHandler());
        registerEvent(new ActionEventAdditionalToolTips());
        registerEvent(new ActionEventAutoplantItemEntity());
        registerEvent(new ActionEventDeathPoint());
        registerEvent(new OnGroundCraftingHandler());
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