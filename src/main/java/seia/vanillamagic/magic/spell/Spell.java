package seia.vanillamagic.magic.spell;

import net.minecraft.item.ItemStack;
import seia.vanillamagic.api.magic.ISpell;
import seia.vanillamagic.api.magic.IWand;
import seia.vanillamagic.util.ItemStackUtil;

/**
 * Basic implementation of Spell.
 * 
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class Spell implements ISpell {
	/**
	 * Spell ID.
	 */
	private final int spellID;
	/**
	 * Name of the Spell.
	 */
	private final String spellName;
	/**
	 * Spell unique name.
	 */
	private final String spellUniqueName;
	/**
	 * Wand required for this Spell.
	 */
	private final IWand wand;
	/**
	 * Required ItemStack in offhand for successfully Spell cast.
	 */
	private final ItemStack itemOffHand;

	public Spell(int spellID, String spellName, String spellUniqueName, IWand wand, ItemStack itemOffHand) {
		this.spellID = spellID;
		this.spellName = spellName;
		this.spellUniqueName = spellUniqueName;
		this.wand = wand;
		this.itemOffHand = itemOffHand;

		SpellRegistry.addSpell(this);
	}

	public int getSpellID() {
		return spellID;
	}

	public String getSpellName() {
		return spellName;
	}

	public String getSpellUniqueName() {
		return spellUniqueName;
	}

	public IWand getWand() {
		return wand;
	}

	public ItemStack getRequiredStackOffHand() {
		return itemOffHand;
	}

	public boolean isItemOffHandRightForSpell(ItemStack stackOffHand) {
		return (itemOffHand.getItem().equals(stackOffHand.getItem()))
				&& (itemOffHand.getMetadata() == stackOffHand.getMetadata())
				&& (ItemStackUtil.getStackSize(itemOffHand) <= ItemStackUtil.getStackSize(stackOffHand));
	}
}