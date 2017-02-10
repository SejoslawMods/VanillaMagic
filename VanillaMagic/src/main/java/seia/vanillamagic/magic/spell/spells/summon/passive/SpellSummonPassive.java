package seia.vanillamagic.magic.spell.spells.summon.passive;

import net.minecraft.item.ItemStack;
import seia.vanillamagic.magic.spell.SpellRegistry;
import seia.vanillamagic.magic.spell.spells.summon.SpellSummon;
import seia.vanillamagic.magic.wand.IWand;

public abstract class SpellSummonPassive extends SpellSummon
{
	public SpellSummonPassive(int spellID, String spellName, String spellUniqueName, IWand wand,
			ItemStack itemOffHand) 
	{
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
		SpellRegistry.addEntityPassive(this);
	}
}