package com.github.sejoslaw.vanillamagic.api.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Base class for Events related to QuestCaptureEntity.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCaptureEntity extends EventPlayerOnWorld {
	private final Entity entity;

	public EventCaptureEntity(World world, PlayerEntity player, Entity entity) {
		super(player, world);
		this.entity = entity;
	}

	/**
	 * @return Returns the Entity which should be captured into book or spawned from
	 *         book.<br>
	 *         In Respawn Event this Entity is pre-created Entity that will be added
	 *         into World if the Event is not canceled.
	 */
	public Entity getEntity() {
		return entity;
	}

	/**
	 * This Event is fired BEFORE Player captures Entity into book.
	 */
	public static class Capture extends EventCaptureEntity {
		public Capture(World world, PlayerEntity player, Entity target) {
			super(world, player, target);
		}
	}

	/**
	 * This Event is fired BEFORE Player respawn the captured Entity.<br>
	 * If there was no Entity or the type is wrong, this Event will not fire.
	 */
	public static class Respawn extends EventCaptureEntity {
		private final BlockPos respawnPos;
		private final Direction face;
		private final String type;

		public Respawn(World world, PlayerEntity player, BlockPos respawnPos, Direction face, Entity entity, String type) {
			super(world, player, entity);
			this.respawnPos = respawnPos;
			this.face = face;
			this.type = type;
		}

		/**
		 * @return Returns the position on which captured Entity will be respawned.
		 */
		public BlockPos getPos() {
			return respawnPos;
		}

		/**
		 * @return Returns the clicked face of the block.
		 */
		public Direction getFace() {
			return face;
		}

		/**
		 * @return Returns the String representing the type of spawned Entity. This is a
		 *         full-class name of the Entity.
		 */
		public String getEntityType() {
			return type;
		}
	}
}