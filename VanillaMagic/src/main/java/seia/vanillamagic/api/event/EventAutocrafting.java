package seia.vanillamagic.api.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.api.tileentity.machine.IAutocrafting;

public class EventAutocrafting extends EventMachine 
{
	private IAutocrafting tileAutocrafting;
	
	public EventAutocrafting(IAutocrafting tileAutocrafting, World world, BlockPos customTilePos) 
	{
		super(tileAutocrafting, world, customTilePos);
		this.tileAutocrafting = tileAutocrafting;
	}
	
	public IAutocrafting getTileAutocrafting()
	{
		return tileAutocrafting;
	}
	
	/**
	 * This Event is fired at the end of TileAutocrafting tick.
	 */
	public static class Work extends EventAutocrafting
	{
		public Work(IAutocrafting tileAutocrafting, World world, BlockPos customTilePos) 
		{
			super(tileAutocrafting, world, customTilePos);
		}
	}
}