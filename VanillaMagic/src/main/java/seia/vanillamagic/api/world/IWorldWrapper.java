package seia.vanillamagic.api.world;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * WorldWrapper is used to hold an information about the {@link World}.
 */
public interface IWorldWrapper 
{
	/**
	 * Returns the World.
	 * @see TileEntity#getWorld()
	 */
	World getWorld();
	
	/**
	 * Sets the World.
	 * @see TileEntity#setWorldObj(World)
	 */
	void setWorld(World world);
}