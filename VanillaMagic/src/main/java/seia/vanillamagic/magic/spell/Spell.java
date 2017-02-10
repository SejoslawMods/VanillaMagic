package seia.vanillamagic.magic.spell;

import net.minecraft.item.ItemStack;
import seia.vanillamagic.magic.wand.IWand;
import seia.vanillamagic.util.ItemStackHelper;

public abstract class Spell implements ISpell
{
	private final int spellID;
	private final String spellName;
	private final String spellUniqueName;
	private final IWand wand;
	private final ItemStack itemOffHand;
	
	public Spell(int spellID, String spellName, String spellUniqueName, IWand wand, ItemStack itemOffHand)
	{
		this.spellID = spellID;
		this.spellName = spellName;
		this.spellUniqueName = spellUniqueName;
		this.wand = wand;
		this.itemOffHand = itemOffHand;
		SpellRegistry.addSpell(this);
	}
	
	public int getSpellID() 
	{
		return spellID;
	}
	
	public String getSpellName() 
	{
		return spellName;
	}
	
	public String getSpellUniqueName() 
	{
		return spellUniqueName;
	}
	
	public IWand getWand() 
	{
		return wand;
	}
	
	public ItemStack getRequiredStackOffHand() 
	{
		return itemOffHand;
	}
	
	public boolean isItemOffHandRightForSpell(ItemStack stackOffHand)
	{
		return (itemOffHand.getItem().equals(stackOffHand.getItem())) && 
				(itemOffHand.getMetadata() == stackOffHand.getMetadata()) &&
				(ItemStackHelper.getStackSize(itemOffHand) <= ItemStackHelper.getStackSize(stackOffHand));
	}
}