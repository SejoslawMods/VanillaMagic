package seia.vanillamagic.api.event;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;

/**
 * This Event is fired BEFORE Entity is teleport in VanillaMagic-way.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventTeleportEntity extends Event {
	private final Entity entity;
	private final BlockPos newPos;

	public EventTeleportEntity(Entity entity, BlockPos newPos) {
		this.entity = entity;
		this.newPos = newPos;
	}

	/**
	 * @return Returns Entity to teleport.
	 */
	public Entity getEntity() {
		return this.entity;
	}

	/**
	 * It may return current Entity position if used in {@link CrossDimension}
	 * teleport.<br>
	 * If yes, than use {@link CrossDimension#getNewDimId()} to check if Dimension
	 * will change.
	 * 
	 * @return Returns new position on which Entity should be teleport.
	 */
	public BlockPos getNewPos() {
		return this.newPos;
	}

	/**
	 * This Event is fired BEFORE Entity is teleport to new Dimension. <br>
	 * To check if Player changed Dimension use Forge Event
	 * {@link PlayerChangedDimensionEvent} or {@link EntityTravelToDimensionEvent}
	 */
	public static class ChangeDimension extends EventTeleportEntity {
		private final int newDimId;

		public ChangeDimension(Entity entity, BlockPos newPos, int newDimId) {
			super(entity, newPos);
			this.newDimId = newDimId;
		}

		/**
		 * To get {@link WorldServer} from Id use
		 * {@link DimensionManager#getWorld(int)}.
		 * 
		 * @return Returns the Id of a new Dimension into which Entity will be teleport.
		 * 
		 * @see {@link DimensionManager#getWorld(int)}
		 */
		public int getNewDimId() {
			return this.newDimId;
		}
	}
}