package seia.vanillamagic.util;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

/**
 * Class which wraps various World related stuff.
 */
public class WorldUtil 
{
	private WorldUtil()
	{
	}
	
	/**
	 * @return Returns the World id.
	 */
	public static int getDimensionID(World world)
	{
		return world.provider.getDimension();
	}
	
	/**
	 * @return Returns the World id.
	 */
	public static int getDimensionID(Entity entity)
	{
		return entity.dimension;
	}
	
	/**
	 * @return Returns the World id.
	 */
	public static int getDimensionID(TileEntity tile)
	{
		return getDimensionID(tile.getWorld());
	}
	
	/**
	 * @return Returns the World id.
	 */
	public static int getDimensionID(WorldEvent event)
	{
		return getDimensionID(event.getWorld());
	}
}