package com.github.sejoslaw.vanillamagic.common.magic.spell.spells.summon.hostile;

import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import com.github.sejoslaw.vanillamagic.core.VMConfig;
import net.minecraft.item.ItemStack;

import java.util.Random;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class SpellSummonHostileWithRandomArmor extends SpellSummonHostile {
	public SpellSummonHostileWithRandomArmor(int spellID, String spellName, String spellUniqueName, IWand wand,
			ItemStack itemOffHand) {
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public boolean getSpawnWithArmor() {
		return new Random().nextInt(100) < VMConfig.PERCENT_FOR_SPAWN_HOSTILE_WITH_ARMOR.get();
	}
}