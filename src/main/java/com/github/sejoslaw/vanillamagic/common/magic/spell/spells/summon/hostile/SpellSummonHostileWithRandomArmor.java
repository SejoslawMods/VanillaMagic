package com.github.sejoslaw.vanillamagic.magic.spell.spells.summon.hostile;

import java.util.Random;

import net.minecraft.item.ItemStack;
import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import com.github.sejoslaw.vanillamagic.config.VMConfig;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class SpellSummonHostileWithRandomArmor extends SpellSummonHostile {
	public SpellSummonHostileWithRandomArmor(int spellID, String spellName, String spellUniqueName, IWand wand,
			ItemStack itemOffHand) {
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public boolean getSpawnWithArmor() {
		int rand = new Random().nextInt(100);

		if (rand < VMConfig.PERCENT_FOR_SPAWN_HOSTILE_WITH_ARMOR) {
			return true;
		}

		return false;
	}
}