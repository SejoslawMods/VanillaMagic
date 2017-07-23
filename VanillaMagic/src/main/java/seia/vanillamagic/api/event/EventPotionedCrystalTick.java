package seia.vanillamagic.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

/**
 * This Event is fired BEFORE PotionedCrystal apply PotionEffect to Player.
 */
public class EventPotionedCrystalTick extends EventPlayerOnWorld
{
	private final ItemStack _crystalStack;
	private final PotionEffect _effectToApply;
	
	public EventPotionedCrystalTick(EntityPlayer player, ItemStack crystalStack, PotionEffect effectToApply) 
	{
		super(player, player.world);
		this._crystalStack = crystalStack;
		this._effectToApply = effectToApply;
	}
	
	/**
	 * @return Returns the PotionedCrystal itself in a form of ItemStack.
	 */
	public ItemStack getPotionedCrystal()
	{
		return this._crystalStack;
	}
	
	/**
	 * @return Returns the PotionEffect which will be applied to Player.
	 */
	public PotionEffect getPotionEffect()
	{
		return this._effectToApply;
	}
}