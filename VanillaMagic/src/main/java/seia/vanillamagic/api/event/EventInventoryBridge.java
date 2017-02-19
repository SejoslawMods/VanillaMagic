package seia.vanillamagic.api.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.api.tileentity.inventorybridge.IInventoryBridge;

/**
 * This Event is fired at the end of TileInventoryBridge single slot transfer.
 */
public class EventInventoryBridge extends EventCustomTileEntity 
{
	private IInventoryBridge tileBridge;
	
	public EventInventoryBridge(IInventoryBridge tileBridge, World world, BlockPos customTilePos) 
	{
		super(tileBridge, world, customTilePos);
		this.tileBridge = tileBridge;
	}
	
	/**
	 * @return Returns the TileInventoryBridge.
	 */
	public IInventoryBridge getInventoryBridge()
	{
		return tileBridge;
	}
}