package seia.vanillamagic.api.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Base class for Events related to QuestCaptureEntity.
 */
public class EventCaptureEntity extends EventPlayerOnWorld 
{
	public EventCaptureEntity(World world, EntityPlayer player)
	{
		super(player, world);
	}
	
	/**
	 * This Event is fired BEFORE Player captures Entity into book.
	 */
	public static class Capture extends EventCaptureEntity
	{
		private final Entity _target;
		
		public Capture(World world, EntityPlayer player, 
				Entity target) 
		{
			super(world, player);
			this._target = target;
		}
		
		/**
		 * @return Returns the Entity which should be captured into book.
		 */
		public Entity getTarget()
		{
			return _target;
		}
	}
	
	/**
	 * This Event is fired BEFORE Player respawn the captured Entity.
	 */
	public static class Respawn extends EventCaptureEntity
	{
		private final BlockPos _respawnPos;
		private final EnumFacing _face;
		
		public Respawn(World world, EntityPlayer player, BlockPos respawnPos, EnumFacing face) 
		{
			super(world, player);
			this._respawnPos = respawnPos;
			this._face = face;
		}
		
		/**
		 * @return Returns the position on which captured Entity will be respawned.
		 */
		public BlockPos getPos()
		{
			return _respawnPos;
		}
		
		/**
		 * @return Returns the clicked face of the block.
		 */
		public EnumFacing getFace()
		{
			return _face;
		}
	}
}