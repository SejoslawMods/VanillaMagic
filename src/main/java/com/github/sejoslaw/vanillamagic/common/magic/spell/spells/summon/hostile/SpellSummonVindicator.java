package com.github.sejoslaw.vanillamagic.magic.spell.spells.summon.hostile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import com.github.sejoslaw.vanillamagic.api.magic.IWand;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellSummonVindicator extends SpellSummonHostile {
	public SpellSummonVindicator(int spellID, String spellName, String spellUniqueName, IWand wand,
			ItemStack itemOffHand) {
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public Entity getEntity(World world) {
		EntityVindicator entity = new EntityVindicator(world);
		entity.setAggressive(true);
		return entity;
	}
}