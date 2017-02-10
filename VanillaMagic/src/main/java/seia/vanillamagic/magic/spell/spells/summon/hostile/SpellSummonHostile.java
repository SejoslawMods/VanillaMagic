package seia.vanillamagic.magic.spell.spells.summon.hostile;

import net.minecraft.item.ItemStack;
import seia.vanillamagic.magic.spell.SpellRegistry;
import seia.vanillamagic.magic.spell.spells.summon.SpellSummon;
import seia.vanillamagic.magic.wand.IWand;

public abstract class SpellSummonHostile extends SpellSummon
{
	public SpellSummonHostile(int spellID, String spellName, String spellUniqueName, IWand wand,
			ItemStack itemOffHand) 
	{
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
		SpellRegistry.addEntityHostile(this);
	}
}