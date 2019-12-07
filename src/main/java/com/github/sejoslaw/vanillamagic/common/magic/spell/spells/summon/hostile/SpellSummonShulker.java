package com.github.sejoslaw.vanillamagic.magic.spell.spells.summon.hostile;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import com.github.sejoslaw.vanillamagic.api.magic.IWand;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellSummonShulker extends SpellSummonHostile {
	public SpellSummonShulker(int spellID, String spellName, String spellUniqueName, IWand wand,
			ItemStack itemOffHand) {
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public Entity getEntity(World world) {
		return new EntityShulker(world);
	}
}