package seia.vanillamagic.magic.spell;

import net.minecraft.item.ItemStack;
import seia.vanillamagic.api.magic.ISpell;
import seia.vanillamagic.api.magic.IWand;
import seia.vanillamagic.util.ItemStackUtil;

/**
 * Basic implementation of Spell.
 */
public abstract class Spell implements ISpell
{
	/**
	 * Spell ID.
	 */
	private final int _spellID;
	/**
	 * Name of the Spell.
	 */
	private final String _spellName;
	/**
	 * Spell unique name.
	 */
	private final String _spellUniqueName;
	/**
	 * Wand required for this Spell.
	 */
	private final IWand _wand;
	/**
	 * Required ItemStack in offhand for successfully Spell cast.
	 */
	private final ItemStack _itemOffHand;
	
	public Spell(int spellID, String spellName, String spellUniqueName, IWand wand, ItemStack itemOffHand)
	{
		this._spellID = spellID;
		this._spellName = spellName;
		this._spellUniqueName = spellUniqueName;
		this._wand = wand;
		this._itemOffHand = itemOffHand;
		SpellRegistry.addSpell(this);
	}
	
	public int getSpellID() 
	{
		return _spellID;
	}
	
	public String getSpellName() 
	{
		return _spellName;
	}
	
	public String getSpellUniqueName() 
	{
		return _spellUniqueName;
	}
	
	public IWand getWand() 
	{
		return _wand;
	}
	
	public ItemStack getRequiredStackOffHand() 
	{
		return _itemOffHand;
	}
	
	public boolean isItemOffHandRightForSpell(ItemStack stackOffHand)
	{
		return (_itemOffHand.getItem().equals(stackOffHand.getItem())) && 
				(_itemOffHand.getMetadata() == stackOffHand.getMetadata()) &&
				(ItemStackUtil.getStackSize(_itemOffHand) <= ItemStackUtil.getStackSize(stackOffHand));
	}
}