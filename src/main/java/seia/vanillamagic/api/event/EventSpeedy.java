package seia.vanillamagic.api.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.api.tileentity.speedy.ISpeedy;

/**
 * This Event is fired at the end of TileSpeedy tick.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventSpeedy extends EventCustomTileEntity {
	private final ISpeedy tileSpeedy;

	public EventSpeedy(ISpeedy tileSpeedy, World world, BlockPos customTilePos) {
		super(tileSpeedy, world, customTilePos);
		this.tileSpeedy = tileSpeedy;
	}

	/**
	 * @return Returns TileSpeedy connected with this Event.
	 */
	public ISpeedy getTileSpeedy() {
		return tileSpeedy;
	}
}