package seia.vanillamagic.util;

import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

public class WorldHelper
{
	private WorldHelper()
	{
	}
	
	public static int getDimensionID(World world)
	{
		return world.provider.getDimension();
	}
	
	public static int getDimensionID(Entity entity)
	{
		return entity.dimension;
	}
	
	public static int getDimensionID(TileEntity tile)
	{
		return getDimensionID(tile.getWorld());
	}
	
	public static int getDimensionID(WorldEvent event)
	{
		return getDimensionID(event.getWorld());
	}
}