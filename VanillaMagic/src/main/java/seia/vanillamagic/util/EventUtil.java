package seia.vanillamagic.util;

import org.apache.logging.log4j.Level;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import seia.vanillamagic.core.VanillaMagic;

/**
 * Various utilities connected with Minecraft Events.
 * Wraps Minecraft Events.
 */
public class EventUtil 
{
	private EventUtil()
	{
	}
	
	/**
	 * @return Returns EntityPlayer from given Event.
	 */
	public static EntityPlayer getPlayerFromEvent(PlayerEvent event)
	{
		return event.getEntityPlayer();
	}
	
	/**
	 * Register New Event.
	 */
	public static void registerEvent(Object o)
	{
		MinecraftForge.EVENT_BUS.register(o);
		VanillaMagic.log(Level.INFO, "Registered Event: " + o.toString());
	}
}