package seia.vanillamagic.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * Base class for all Events that connects Player and World.
 */
public class EventPlayerOnWorld extends Event
{
	private final EntityPlayer _player;
	private final World _world;
	
	public EventPlayerOnWorld(EntityPlayer player, World world)
	{
		this._player = player;
		this._world = world;
	}
	
	/**
	 * @return Returns Player who started this Event.
	 */
	public EntityPlayer getEntityPlayer()
	{
		return _player;
	}
	
	/**
	 * @return Returns World on which Player started this Event.
	 */
	public World getWorld()
	{
		return _world;
	}
}