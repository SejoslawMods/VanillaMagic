package seia.vanillamagic.api.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.api.tileentity.machine.IFarm;

public class EventFarm extends EventMachine 
{
	private final IFarm _tileFarm;

	public EventFarm(IFarm tileFarm, World world, BlockPos customTilePos) 
	{
		super(tileFarm, world, customTilePos);
		this._tileFarm = tileFarm;
	}
	
	/**
	 * @return Returns the TileFarm.
	 */
	public IFarm getTileFarm()
	{
		return _tileFarm;
	}
	
	/**
	 * This Event is fired at the end of TileFarm tick. After all the work has been done.
	 */
	public static class Work extends EventFarm
	{
		public Work(IFarm tileFarm, World world, BlockPos customTilePos) 
		{
			super(tileFarm, world, customTilePos);
		}
	}
}