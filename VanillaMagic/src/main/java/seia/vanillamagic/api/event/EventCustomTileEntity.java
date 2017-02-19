package seia.vanillamagic.api.event;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;
import seia.vanillamagic.api.tileentity.ICustomTileEntity;

/**
 * Event which is fired when something happens connected with {@link ICustomTileEntity}.
 */
public class EventCustomTileEntity extends Event
{
	private ICustomTileEntity customTileEntity;
	private World world;
	private BlockPos customTilePos;
	
	public EventCustomTileEntity(ICustomTileEntity customTileEntity, World world, BlockPos customTilePos)
	{
		this.customTileEntity = customTileEntity;
		this.world = world;
		this.customTileEntity = customTileEntity;
	}
	
	/**
	 * @return Returns ICustomTileEntity connected with this Event. NULL after this Tile has been deleted from World.
	 */
	@Nullable
	public ICustomTileEntity getCustomTileEntity()
	{
		return customTileEntity;
	}
	
	/**
	 * @return Returns the World on which ICustomTileEntity is.
	 */
	public World getWorld()
	{
		return world;
	}
	
	/**
	 * @return Returns the position of ICustomTileEntity.
	 */
	public BlockPos getTilePos()
	{
		return customTilePos;
	}
	
	/**
	 * This Event is fired when something connected with adding ICustomTileEntity to the World happens.
	 */
	public static class Add extends EventCustomTileEntity
	{
		public Add(ICustomTileEntity customTileEntity, World world, BlockPos customTilePos) 
		{
			super(customTileEntity, world, customTilePos);
		}
		
		/**
		 * This Event is fired BEFORE the ICustomTileEntity is added to World at given position.
		 */
		public static class Before extends Add
		{
			public Before(ICustomTileEntity customTileEntity, World world, BlockPos customTilePos) 
			{
				super(customTileEntity, world, customTilePos);
			}
		}
		
		/**
		 * This Event is fired AFTER the ICustomTileEntity has been added to World at given position.
		 */
		public static class After extends Add
		{
			public After(ICustomTileEntity customTileEntity, World world, BlockPos customTilePos) 
			{
				super(customTileEntity, world, customTilePos);
			}
		}
	}
	
	/**
	 * This Event is fired when something connected with removing TileEntity from the World happens.
	 */
	public static class Remove extends EventCustomTileEntity
	{
		public Remove(ICustomTileEntity customTileEntity, World world, BlockPos pos) 
		{
			super(customTileEntity, world, pos);
		}
		
		/**
		 * This Event is fired BEFORE the TileEntity is removed from World at given position.
		 */
		public static class Before extends Remove
		{
			public Before(ICustomTileEntity customTileEntity, World world, BlockPos pos) 
			{
				super(customTileEntity, world, pos);
			}
			
			/**
			 * This Event is fired BEFORE the TileEntity is removed from World at given position 
			 * and also the information about it is send to Player.
			 */
			public static class SendInfoToPlayer extends Before
			{
				private EntityPlayer player;
				
				public SendInfoToPlayer(ICustomTileEntity customTileEntity, World world, BlockPos pos, EntityPlayer player) 
				{
					super(customTileEntity, world, pos);
				}
				
				/**
				 * @return Returns the Player which will receive an information.
				 */
				public EntityPlayer getEntityPlayer()
				{
					return player;
				}
			}
		}
		
		/**
		 * This Event is fired AFTER the TileEntity has been removed from World at given position.<br>
		 * ICustomTileEntity is NULL !!!
		 */
		public static class After extends Remove
		{
			public After(ICustomTileEntity customTileEntity, World world, BlockPos pos) 
			{
				super(customTileEntity, world, pos);
			}
			
			/**
			 * This Event is fired AFTER the TileEntity has been removed from World at given position 
			 * and also the information about it has been send to Player.
			 */
			public static class SendInfoToPlayer extends After
			{
				private EntityPlayer player;
				
				public SendInfoToPlayer(ICustomTileEntity customTileEntity, World world, BlockPos pos, EntityPlayer player) 
				{
					super(customTileEntity, world, pos);
				}
				
				/**
				 * @return Returns the Player which received an information.
				 */
				public EntityPlayer getEntityPlayer()
				{
					return player;
				}
			}
		}
	}
}