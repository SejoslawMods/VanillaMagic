package seia.vanillamagic.api.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.api.tileentity.speedy.ISpeedy;

/**
 * This Event is fired at the end of TileSpeedy tick.
 */
public class EventSpeedy extends EventCustomTileEntity 
{
	private final ISpeedy _tileSpeedy;
	
	public EventSpeedy(ISpeedy tileSpeedy, World world, BlockPos customTilePos) 
	{
		super(tileSpeedy, world, customTilePos);
		this._tileSpeedy = tileSpeedy;
	}
	
	public ISpeedy getTileSpeedy()
	{
		return _tileSpeedy;
	}
}