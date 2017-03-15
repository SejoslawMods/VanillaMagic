package seia.vanillamagic.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Base class for Events connected with Quest Jumper.
 */
public class EventJumper extends EventPlayerOnWorld
{
	private final BlockPos _pos;
	
	public EventJumper(EntityPlayer player, World world, BlockPos pos) 
	{
		super(player, world);
		this._pos = pos;
	}
	
	/**
	 * It can be either position which Player wants to save OR position on which Player wants to teleport. 
	 * 
	 * @return Returns position.
	 */
	public BlockPos getPos()
	{
		return this._pos;
	}
	
	/**
	 * This Event is fired when Player wants to save block position into book.
	 */
	public static class SavePosition extends EventJumper 
	{
		public SavePosition(EntityPlayer player, World world, BlockPos pos) 
		{
			super(player, world, pos);
		}
		
		/**
		 * This Event is fired BEFORE Player save position into book.
		 */
		public static class Before extends SavePosition
		{
			public Before(EntityPlayer player, World world, BlockPos pos) 
			{
				super(player, world, pos);
			}
		}
		
		/**
		 * This Event is fired AFTER Player saved position into book.
		 */
		public static class After extends SavePosition
		{
			public After(EntityPlayer player, World world, BlockPos pos) 
			{
				super(player, world, pos);
			}
		}
	}
	
	/**
	 * This Event is fired when Player wants to Teleport.
	 */
	public static class Teleport extends EventJumper
	{
		private final int _destinationDimId;
		
		public Teleport(EntityPlayer player, World world, BlockPos pos, int dimId) 
		{
			super(player, world, pos);
			this._destinationDimId = dimId;
		}
		
		/**
		 * For getting the current dimension of Player use {@link #getWorld()}
		 * 
		 * @return Returns id of a dimension to which Player wants to teleport.
		 */
		public int getDestinationDimensionId()
		{
			return this._destinationDimId;
		}
		
		/**
		 * This Event is fired BEFORE Player teleports.
		 */
		public static class Before extends Teleport
		{
			public Before(EntityPlayer player, World world, BlockPos pos, int dimId) 
			{
				super(player, world, pos, dimId);
			}
		}
		
		/**
		 * This Event is fired AFTER Player teleports.
		 */
		public static class After extends Teleport
		{
			public After(EntityPlayer player, World world, BlockPos pos, int dimId) 
			{
				super(player, world, pos, dimId);
			}
		}
	}
}