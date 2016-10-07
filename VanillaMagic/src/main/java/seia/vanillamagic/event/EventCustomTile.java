package seia.vanillamagic.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import seia.vanillamagic.api.tileentity.ICustomTileEntity;

/**
 * Events based on this {@link WorldEvent} are generally fired BEFORE the specialized action.
 */
public class EventCustomTile extends WorldEvent
{
	public final ICustomTileEntity customTileEntity;
	public final int dimensionID;
	
	public EventCustomTile(ICustomTileEntity customTileEntity, int dimensionID) 
	{
		super(customTileEntity.getWorld());
		this.customTileEntity = customTileEntity;
		this.dimensionID = dimensionID;
	}
	
	/**
	 * This {@link WorldEvent} is fired BEFORE the {@link ICustomTileEntity} is added to World and WorldHandler.
	 */
	public static class EventAddCustomTile extends EventCustomTile
	{
		public EventAddCustomTile(ICustomTileEntity customTileEntity, int dimensionID) 
		{
			super(customTileEntity, dimensionID);
		}
	}
	
	/**
	 * This {@link WorldEvent} is fired BEFORE the {@link ICustomTileEntity} at given position is removed from World and WorldHandler.
	 */
	public static class EventRemoveCustomTile extends EventCustomTile
	{
		public final BlockPos removePos;
		
		public EventRemoveCustomTile(World world, BlockPos pos, int dimension) 
		{
			super((ICustomTileEntity)world.getTileEntity(pos), dimension);
			this.removePos = pos;
		}
	}
}