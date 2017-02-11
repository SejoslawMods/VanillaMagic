package seia.vanillamagic.magic.spell;

import net.minecraft.item.ItemStack;
import seia.vanillamagic.magic.wand.IWand;
import seia.vanillamagic.util.ItemStackHelper;

public abstract class Spell implements ISpell
{
	private final int _spellID;
	private final String _spellName;
	private final String _spellUniqueName;
	private final IWand _wand;
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
				(ItemStackHelper.getStackSize(_itemOffHand) <= ItemStackHelper.getStackSize(stackOffHand));
	}
}