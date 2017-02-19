package seia.vanillamagic.api.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;
import seia.vanillamagic.api.tileentity.machine.IMachine;

/**
 * This is the base Event for all Machine-based Events.
 */
public class EventMachine extends EventCustomTileEntity
{
	private IMachine tileMachine;
	
	public EventMachine(IMachine tileMachine, World world, BlockPos customTilePos)
	{
		super(tileMachine, world, customTilePos);
		this.tileMachine = tileMachine;
	}
	
	public IMachine getTileMachine()
	{
		return tileMachine;
	}
	
	/**
	 * This {@link Event} is fired BEFORE the given {@link IMachine} performs it's work.
	 */
	public static class Work extends EventMachine
	{
		public Work(IMachine tileMachine, World world, BlockPos customTilePos) 
		{
			super(tileMachine, world, customTilePos);
		}
	}
}