package seia.vanillamagic.event;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;

/**
 * Events based on this {@link WorldEvent} are generally fired BEFORE the specialized action.
 */
public class EventCustomTile extends WorldEvent
{
	public final TileEntity customTileEntity;
	public final int dimensionID;
	
	public EventCustomTile(TileEntity customTileEntity, int dimensionID) 
	{
		super(customTileEntity.getWorld());
		this.customTileEntity = customTileEntity;
		this.dimensionID = dimensionID;
	}
	
	/**
	 * This {@link WorldEvent} is fired BEFORE the {@link TileEntity} is added to World and WorldHandler.
	 */
	public static class EventAddCustomTile extends EventCustomTile
	{
		public EventAddCustomTile(TileEntity customTileEntity, int dimensionID) 
		{
			super(customTileEntity, dimensionID);
		}
	}
	
	/**
	 * This {@link WorldEvent} is fired BEFORE the {@link TileEntity} at given position is removed from World and WorldHandler.
	 */
	public static class EventRemoveCustomTile extends EventCustomTile
	{
		public final BlockPos removePos;
		
		public EventRemoveCustomTile(World world, BlockPos pos, int dimension) 
		{
			super(world.getTileEntity(pos), dimension);
			this.removePos = pos;
		}
	}
}