package seia.vanillamagic.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Base class for all Events that connects Player and World.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventPlayerOnWorld extends Event {
	private final EntityPlayer player;
	private final World world;

	public EventPlayerOnWorld(EntityPlayer player, World world) {
		this.player = player;
		this.world = world;
	}

	/**
	 * @return Returns Player who started this Event.
	 */
	public EntityPlayer getEntityPlayer() {
		return player;
	}

	/**
	 * @return Returns World on which Player started this Event.
	 */
	public World getWorld() {
		return world;
	}
}