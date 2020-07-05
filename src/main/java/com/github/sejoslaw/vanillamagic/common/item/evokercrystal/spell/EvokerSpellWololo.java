package com.github.sejoslaw.vanillamagic.common.item.evokercrystal.spell;

import com.github.sejoslaw.vanillamagic.api.magic.IEvokerSpell;
import com.github.sejoslaw.vanillamagic.common.util.ListUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.util.SoundEvents;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EvokerSpellWololo implements IEvokerSpell {
	final Predicate<SheepEntity> wololoSelector = sheep -> true;

	public int getSpellID() {
		return 3;
	}

	public String getSpellName() {
		return "Wololo";
	}

	public void castSpell(Entity fakeEvoker, Entity target) {
		if (fakeEvoker == null) {
			return;
		}

		List<SheepEntity> list = fakeEvoker.world.getEntitiesWithinAABB(SheepEntity.class, fakeEvoker.getBoundingBox().expand(16.0D, 4.0D, 16.0D), wololoSelector);

		if (!list.isEmpty()) {
			Random rand = new Random();
			SheepEntity sheep = list.get(rand.nextInt(list.size()));

			if (sheep != null && sheep.isAlive()) {
				sheep.setFleeceColor(ListUtil.getRandomObjectFromTab(DyeColor.values()));
			}
		}
		fakeEvoker.playSound(SoundEvents.ENTITY_EVOKER_PREPARE_WOLOLO, 1.0F, 1.0F);
	}
}
