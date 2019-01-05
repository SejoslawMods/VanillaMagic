package seia.vanillamagic.util;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Various utilities connected with Minecraft Events. Wraps Minecraft Events.
 */
public class EventUtil {
	private EventUtil() {
	}

	/**
	 * @return Returns EntityPlayer from given Event.
	 */
	public static EntityPlayer getPlayerFromEvent(PlayerEvent event) {
		return event.getEntityPlayer();
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