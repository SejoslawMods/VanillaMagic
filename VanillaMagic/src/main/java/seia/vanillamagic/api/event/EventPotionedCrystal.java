package seia.vanillamagic.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import seia.vanillamagic.api.item.IPotionedCrystal;

public class EventPotionedCrystal extends EventCustomItem.OnUseByPlayer
{
	private final IPotionedCrystal _crystal;
	
	public EventPotionedCrystal(IPotionedCrystal usedItem, EntityPlayer user, World world, BlockPos usedOnPos) 
	{
		super(usedItem, user, world, usedOnPos);
		this._crystal = usedItem;
	}
	
	/**
	 * @return Returns used Potioned Crystal.
	 */
	public IPotionedCrystal getCrystal()
	{
		return _crystal;
	}
	
	/**
	 * This Event is fired BEFORE Player gets the given Potion Effect.
	 */
	public static class AddPotionEffect extends EventPotionedCrystal
	{
		private final PotionEffect _potionEffect;
		
		public AddPotionEffect(IPotionedCrystal usedItem, EntityPlayer user, World world, BlockPos usedOnPos,
				PotionEffect pe) 
		{
			super(usedItem, user, world, usedOnPos);
			this._potionEffect = pe;
		}
		
		/**
		 * @return Returns effect which should be applied to Player.
		 */
		public PotionEffect getEffect()
		{
			return _potionEffect;
		}
	}
}