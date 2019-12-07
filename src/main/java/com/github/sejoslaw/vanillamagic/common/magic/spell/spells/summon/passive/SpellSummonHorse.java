package com.github.sejoslaw.vanillamagic.magic.spell.spells.summon.passive;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityDonkey;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMule;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import com.github.sejoslaw.vanillamagic.api.magic.IWand;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellSummonHorse extends SpellSummonPassive {
	public SpellSummonHorse(int spellID, String spellName, String spellUniqueName, IWand wand, ItemStack itemOffHand) {
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public Entity getEntity(World world) {
		Entity entity = null;
		int rand = this.getPercent();

		if (rand < 30) {
			entity = new EntityDonkey(world);
		} else if (rand > 30 && rand < 60) {
			entity = new EntityMule(world);
		} else {
			entity = new EntityHorse(world);
		}

		return entity;
	}
}