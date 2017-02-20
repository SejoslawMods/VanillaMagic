package seia.vanillamagic.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.api.item.ICustomItem;

/**
 * Base class for all Acceleration Crystal related events.
 */
public class EventAccelerationCrystal extends EventCustomItem.OnUseByPlayer
{
	public EventAccelerationCrystal(ICustomItem customItem, World world, BlockPos clickedPos, EntityPlayer player) 
	{
		super(customItem, player, world, clickedPos);
	}
	
	/**
	 * This Event is fired when Acceleration Crystal speeds up normal block.
	 */
	public static class TickBlock extends EventAccelerationCrystal
	{
		public TickBlock(ICustomItem customItem, World world, BlockPos clickedPos, EntityPlayer player) 
		{
			super(customItem, world, clickedPos, player);
		}
	}
	
	/**
	 * This Event is fired when Acceleration Crystal speeds up TileEntity.
	 */
	public static class TickTileEntity extends EventAccelerationCrystal
	{
		private final TileEntity _clickedTile;
		
		public TickTileEntity(ICustomItem customItem, World world, BlockPos clickedPos, EntityPlayer player, TileEntity clickedTile) 
		{
			super(customItem, world, clickedPos, player);
			this._clickedTile = clickedTile;
		}
		
		/**
		 * @return Returns the TileEntity which was clicked with Acceleration Crystal. This should always be ITickable.
		 */
		public TileEntity getTileEntity()
		{
			return _clickedTile;
		}
	}
}