package com.github.sejoslaw.vanillamagic.magic.spell.spells.summon.hostile;

import net.minecraft.item.ItemStack;
import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import com.github.sejoslaw.vanillamagic.magic.spell.SpellRegistry;
import com.github.sejoslaw.vanillamagic.magic.spell.spells.summon.SpellSummon;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class SpellSummonHostile extends SpellSummon {
	public SpellSummonHostile(int spellID, String spellName, String spellUniqueName, IWand wand,
			ItemStack itemOffHand) {
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
		SpellRegistry.addEntityHostile(this);
	}
}