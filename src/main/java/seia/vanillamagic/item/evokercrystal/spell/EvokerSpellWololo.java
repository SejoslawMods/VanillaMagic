package seia.vanillamagic.item.evokercrystal.spell;

import java.util.List;
import java.util.Random;

import com.google.common.base.Predicate;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumDyeColor;
import seia.vanillamagic.api.magic.IEvokerSpell;
import seia.vanillamagic.util.ListUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EvokerSpellWololo implements IEvokerSpell {
	final Predicate<EntitySheep> wololoSelector = new Predicate<EntitySheep>() {
		public boolean apply(EntitySheep sheep) {
			return true;
		}
	};

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

		List<EntitySheep> list = fakeEvoker.world.<EntitySheep>getEntitiesWithinAABB(EntitySheep.class,
				fakeEvoker.getEntityBoundingBox().expand(16.0D, 4.0D, 16.0D), wololoSelector);

		if (!list.isEmpty()) {
			Random rand = new Random();
			EntitySheep entitySheep = (EntitySheep) list.get(rand.nextInt(list.size()));

			if (entitySheep != null && entitySheep.isEntityAlive()) {
				entitySheep.setFleeceColor(ListUtil.<EnumDyeColor>getRandomObjectFromTab(EnumDyeColor.values()));
			}
		}
		fakeEvoker.playSound(SoundEvents.EVOCATION_ILLAGER_PREPARE_WOLOLO, 1.0F, 1.0F);
	}
}