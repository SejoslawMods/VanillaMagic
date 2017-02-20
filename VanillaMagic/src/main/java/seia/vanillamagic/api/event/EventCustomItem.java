package seia.vanillamagic.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;
import seia.vanillamagic.api.item.ICustomItem;

/**
 * Base class for all Events connected with CustomItems.
 */
public class EventCustomItem extends Event 
{
	private final ICustomItem _customItem;
	
	public EventCustomItem(ICustomItem customItem)
	{
		this._customItem = customItem;
	}
	
	/**
	 * @return Returns the CustomItem connected with this Event.
	 */
	public ICustomItem getCustomItem()
	{
		return _customItem;
	}
	
	/**
	 * Thie Event is fired when something connected with using CustomItem happened.
	 */
	public static class OnUseByPlayer extends EventCustomItem
	{
		private final EntityPlayer _user;
		private final World _world;
		private final BlockPos _usedOnPos;
		
		public OnUseByPlayer(ICustomItem usedItem, EntityPlayer user, World world, BlockPos usedOnPos)
		{
			super(usedItem);
			this._user = user;
			this._world = world;
			this._usedOnPos = usedOnPos;
		}
		
		/**
		 * @return Returns Player who used CustomItem.
		 */
		public EntityPlayer getEntityPlayer()
		{
			return _user;
		}
		
		/**
		 * @return Returns World on which event occurred.
		 */
		public World getWorld()
		{
			return _world;
		}
		
		/**
		 * @return Returns position on which CustomItem was used.
		 */
		public BlockPos getPos()
		{
			return _usedOnPos;
		}
		
		/**
		 * This Event is fired when player use CustomItem on TileEntity.
		 */
		public static class OnTileEntity extends OnUseByPlayer
		{
			private final TileEntity _tile;
			
			public OnTileEntity(ICustomItem usedItem, EntityPlayer user, World world, BlockPos usedOnPos, TileEntity tile) 
			{
				super(usedItem, user, world, usedOnPos);
				this._tile = tile;
			}
			
			/**
			 * @return Returns TileEntity on which this CustomItem was used.
			 */
			public TileEntity getTileEntity()
			{
				return _tile;
			}
		}
	}
}