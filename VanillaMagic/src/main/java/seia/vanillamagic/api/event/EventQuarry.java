package seia.vanillamagic.api.event;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.api.tileentity.machine.IQuarry;
import seia.vanillamagic.api.tileentity.machine.IQuarryUpgrade;

/**
 * Base for all Quarry Events.
 */
public class EventQuarry extends EventMachine 
{
	private final IQuarry _tileQuarry;
	
	public EventQuarry(IQuarry tileQuarry, World world, BlockPos customTilePos) 
	{
		super(tileQuarry, world, customTilePos);
		this._tileQuarry = tileQuarry;
	}
	
	/**
	 * @return Returns the TileQuarry.
	 */
	public IQuarry getTileQuarry()
	{
		return _tileQuarry;
	}
	
	/**
	 * This Event is fired when a new Quarry has been build correctly on World.
	 */
	public static class BuildCorrectly extends EventQuarry
	{
		public BuildCorrectly(IQuarry tileQuarry, World world, BlockPos customTilePos) 
		{
			super(tileQuarry, world, customTilePos);
		}
	}
	
	/**
	 * This Event is fired at the end of TileQuarry tick.
	 */
	public static class Work extends EventQuarry
	{
		public Work(IQuarry tileQuarry, World world, BlockPos customTilePos) 
		{
			super(tileQuarry, world, customTilePos);
		}
	}
	
	/**
	 * This is base for all IQuarryUpgrade-related Events.
	 */
	public static class EventQuarryUpgrade extends EventQuarry
	{
		private final IQuarryUpgrade _upgrade;
		
		public EventQuarryUpgrade(IQuarry tileQuarry, World world, BlockPos customTilePos, IQuarryUpgrade upgrade)
		{
			super(tileQuarry, world, customTilePos);
			this._upgrade = upgrade;
		}
		
		/**
		 * @return Returns the IQuarryUpgrade connected with this Event.
		 */
		public IQuarryUpgrade getUpgrade()
		{
			return _upgrade;
		}
	}
	
	/**
	 * This Event is fired when Quarry added correctly the given upgrade.
	 */
	public static class AddUpgrade extends EventQuarryUpgrade
	{
		public AddUpgrade(IQuarry tileQuarry, World world, BlockPos customTilePos, IQuarryUpgrade upgrade)
		{
			super(tileQuarry, world, customTilePos, upgrade);
		}
	}
	
	public static class ModifyQuarry extends EventQuarryUpgrade
	{
		public ModifyQuarry(IQuarry tileQuarry, World world, BlockPos customTilePos, IQuarryUpgrade upgrade) 
		{
			super(tileQuarry, world, customTilePos, upgrade);
		}
		
		/**
		 * This Event is fired BEFORE the QuarryUpgrade modifies the Quarry.
		 */
		public static class Before extends ModifyQuarry
		{
			public Before(IQuarry tileQuarry, World world, BlockPos customTilePos, IQuarryUpgrade upgrade) 
			{
				super(tileQuarry, world, customTilePos, upgrade);
			}
		}
		
		/**
		 * This Event is fired AFTER the QuarryUpgrade modified the Quarry.
		 */
		public static class After extends ModifyQuarry
		{
			public After(IQuarry tileQuarry, World world, BlockPos customTilePos, IQuarryUpgrade upgrade) 
			{
				super(tileQuarry, world, customTilePos, upgrade);
			}
		}
	}
}