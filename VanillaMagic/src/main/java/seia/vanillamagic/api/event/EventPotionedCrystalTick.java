package seia.vanillamagic.api.event;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.Event;

/**
 * This Event is fired BEFORE PotionedCrystal apply PotionEffect to Player.
 */
public class EventPotionedCrystalTick extends Event
{
	private final EntityPlayer _player;
	private final ItemStack _crystalStack;
	private final PotionEffect _effectToApply;
	
	public EventPotionedCrystalTick(EntityPlayer player, ItemStack crystalStack, PotionEffect effectToApply) 
	{
		this._player = player;
		this._crystalStack = crystalStack;
		this._effectToApply = effectToApply;
	}
	
	/**
	 * @return Returns Player for which the effect will be applied.
	 */
	public EntityPlayer getEntityPlayer()
	{
		return this._player;
	}
	
	/**
	 * @return Returns the World of the Player.
	 */
	public World getWorld()
	{
		return this._player.world;
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