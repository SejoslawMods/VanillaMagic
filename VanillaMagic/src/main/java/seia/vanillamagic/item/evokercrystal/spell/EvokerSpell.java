package seia.vanillamagic.item.evokercrystal.spell;

import seia.vanillamagic.item.evokercrystal.EvokerSpellRegistry;

public abstract class EvokerSpell implements IEvokerSpell
{
	public EvokerSpell()
	{
		EvokerSpellRegistry.add(this);
	}
}