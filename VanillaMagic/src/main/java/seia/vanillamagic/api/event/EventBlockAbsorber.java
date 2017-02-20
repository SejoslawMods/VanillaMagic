package seia.vanillamagic.api.event;

import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.api.tileentity.blockabsorber.IBlockAbsorber;

/**
 * This Event is fired BEFORE the TileBlockAbsorber changed upper block to air.
 */
public class EventBlockAbsorber extends EventCustomTileEntity
{
	private final IBlockAbsorber _tileAbsorber;
	private final TileEntityHopper _connectedHopper;
	
	public EventBlockAbsorber(IBlockAbsorber tileAbsorber, World world, BlockPos tilePos, TileEntityHopper connectedHopper) 
	{
		super(tileAbsorber, world, tilePos);
		this._tileAbsorber = tileAbsorber;
		this._connectedHopper = connectedHopper;
	}
	
	/**
	 * @return Returns the TileBlockAbsorber.
	 */
	public IBlockAbsorber getTileAbsorber()
	{
		return _tileAbsorber;
	}
	
	/**
	 * @return Returns the Hopper which is connected with this TileBlockAbsorber.
	 */
	public TileEntityHopper getConnectedHopper()
	{
		return _connectedHopper;
	}
}