package seia.vanillamagic.api.world;

import net.minecraft.world.World;

/**
 * WorldWrapper is used to hold an information about the {@link World}.
 */
public interface IWorldWrapper 
{
	/**
	 * Returns the World.
	 */
	World getWorld();
	
	/**
	 * Sets the World.
	 */
	void setWorld(World world);
}