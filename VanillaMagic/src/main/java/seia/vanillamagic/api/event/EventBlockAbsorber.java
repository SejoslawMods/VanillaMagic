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
	private IBlockAbsorber tileAbsorber;
	private TileEntityHopper connectedHopper;
	
	public EventBlockAbsorber(IBlockAbsorber tileAbsorber, World world, BlockPos tilePos, TileEntityHopper connectedHopper) 
	{
		super(tileAbsorber, world, tilePos);
		this.tileAbsorber = tileAbsorber;
		this.connectedHopper = connectedHopper;
	}
	
	/**
	 * @return Returns the TileBlockAbsorber.
	 */
	public IBlockAbsorber getTileAbsorber()
	{
		return tileAbsorber;
	}
	
	/**
	 * @return Returns the Hopper which is connected with this TileBlockAbsorber.
	 */
	public TileEntityHopper getConnectedHopper()
	{
		return connectedHopper;
	}
}