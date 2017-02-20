package seia.vanillamagic.item.evokercrystal.spell;

import seia.vanillamagic.api.magic.IEvokerSpell;
import seia.vanillamagic.item.evokercrystal.EvokerSpellRegistry;

public abstract class EvokerSpell implements IEvokerSpell
{
	public EvokerSpell()
	{
		EvokerSpellRegistry.add(this);
	}
}