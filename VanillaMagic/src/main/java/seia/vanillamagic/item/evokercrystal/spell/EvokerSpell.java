package seia.vanillamagic.item.evokercrystal.spell;

import seia.vanillamagic.api.magic.IEvokerSpell;
import seia.vanillamagic.item.evokercrystal.EvokerSpellRegistry;

/**
 * Base implementation of IEvokerSpell.
 */
public abstract class EvokerSpell implements IEvokerSpell
{
	public EvokerSpell()
	{
		// Automatically add new spell to registry.
		EvokerSpellRegistry.add(this);
	}
}