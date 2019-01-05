package seia.vanillamagic.api.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.api.tileentity.machine.IAutocrafting;

/**
 * Event connected with Autocrafting Structure.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventAutocrafting extends EventMachine {
	/**
	 * Connected Autocrafting Structure
	 */
	private final IAutocrafting tileAutocrafting;

	public EventAutocrafting(IAutocrafting tileAutocrafting, World world, BlockPos customTilePos) {
		super(tileAutocrafting, world, customTilePos);
		this.tileAutocrafting = tileAutocrafting;
	}

	/**
	 * @return Returns the Autocrafting Structure Tile.
	 */
	public IAutocrafting getTileAutocrafting() {
		return tileAutocrafting;
	}

	/**
	 * This Event is fired at the end of TileAutocrafting tick.
	 */
	public static class Work extends EventAutocrafting {
		public Work(IAutocrafting tileAutocrafting, World world, BlockPos customTilePos) {
			super(tileAutocrafting, world, customTilePos);
		}
	}
}