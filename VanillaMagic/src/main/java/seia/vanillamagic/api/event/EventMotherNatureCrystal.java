package seia.vanillamagic.api.event;

import net.minecraft.block.IGrowable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import seia.vanillamagic.api.item.ICustomItem;

/**
 * Base class for all Mother Nature Crystal related events.
 */
public class EventMotherNatureCrystal extends EventCustomItem.OnUseByPlayer 
{
	public EventMotherNatureCrystal(ICustomItem usedItem, EntityPlayer user, World world, BlockPos usedOnPos) 
	{
		super(usedItem, user, world, usedOnPos);
	}
	
	/**
	 * This Event is fired BEFORE block is ticked.
	 */
	public static class TickBlock extends EventMotherNatureCrystal
	{
		public TickBlock(ICustomItem usedItem, EntityPlayer user, World world, BlockPos usedOnPos) 
		{
			super(usedItem, user, world, usedOnPos);
		}
	}
	
	/**
	 * This Event is fired BEFORE Growable grow.
	 */
	public static class Grow extends EventMotherNatureCrystal
	{
		private final FakePlayer _fakePlayer;
		private final IGrowable _growable;
		
		public Grow(ICustomItem usedItem, FakePlayer fakePlayer, World world, BlockPos clickedPos,
				IGrowable growable) 
		{
			super(usedItem, fakePlayer, world, clickedPos);
			this._fakePlayer = fakePlayer;
			this._growable = growable;
		}
		
		/**
		 * @return Returns FakePlayer who apply bonemeal effect.
		 */
		public FakePlayer getFakePlayer()
		{
			return _fakePlayer;
		}
		
		/**
		 * @return Returns growable on which the effect will be used.
		 */
		public IGrowable getGrowable()
		{
			return _growable;
		}
	}
}