package com.github.sejoslaw.vanillamagic.common.magic.spell.spells.summon.passive;

import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import com.github.sejoslaw.vanillamagic.common.magic.spell.SpellRegistry;
import com.github.sejoslaw.vanillamagic.common.magic.spell.spells.summon.SpellSummon;
import net.minecraft.item.ItemStack;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class SpellSummonPassive extends SpellSummon {
	public SpellSummonPassive(int spellID, String spellName, String spellUniqueName, IWand wand, ItemStack itemOffHand) {
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
		SpellRegistry.addEntityPassive(this);
	}
}