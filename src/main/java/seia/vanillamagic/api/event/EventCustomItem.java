package seia.vanillamagic.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;
import seia.vanillamagic.api.item.ICustomItem;

/**
 * Base class for all Events connected with CustomItems.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCustomItem extends Event {
	private final ICustomItem customItem;

	public EventCustomItem(ICustomItem customItem) {
		this.customItem = customItem;
	}

	/**
	 * @return Returns the CustomItem connected with this Event.
	 */
	public ICustomItem getCustomItem() {
		return customItem;
	}

	/**
	 * Thie Event is fired when something connected with using CustomItem happened.
	 */
	public static class OnUseByPlayer extends EventCustomItem {
		private final EntityPlayer user;
		private final World world;
		private final BlockPos usedOnPos;

		public OnUseByPlayer(ICustomItem usedItem, EntityPlayer user, World world, BlockPos usedOnPos) {
			super(usedItem);
			this.user = user;
			this.world = world;
			this.usedOnPos = usedOnPos;
		}

		/**
		 * @return Returns Player who used CustomItem.
		 */
		public EntityPlayer getEntityPlayer() {
			return user;
		}

		/**
		 * @return Returns World on which event occurred.
		 */
		public World getWorld() {
			return world;
		}

		/**
		 * @return Returns position on which CustomItem was used.
		 */
		public BlockPos getPos() {
			return usedOnPos;
		}

		/**
		 * This Event is fired when player use CustomItem on TileEntity.
		 */
		public static class OnTileEntity extends OnUseByPlayer {
			private final TileEntity tile;

			public OnTileEntity(ICustomItem usedItem, EntityPlayer user, World world, BlockPos usedOnPos,
					TileEntity tile) {
				super(usedItem, user, world, usedOnPos);
				this.tile = tile;
			}

			/**
			 * @return Returns TileEntity on which this CustomItem was used.
			 */
			public TileEntity getTileEntity() {
				return tile;
			}
		}
	}
}