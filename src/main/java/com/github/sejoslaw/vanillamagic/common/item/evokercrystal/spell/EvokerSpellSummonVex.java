package com.github.sejoslaw.vanillamagic.common.item.evokercrystal.spell;

import com.github.sejoslaw.vanillamagic.api.magic.IEvokerSpell;
import com.github.sejoslaw.vanillamagic.core.VMConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EvokerSpellSummonVex implements IEvokerSpell {
	public int getSpellID() {
		return 1;
	}

	public String getSpellName() {
		return "Summon Vex";
	}

	public void castSpell(Entity fakeEvoker, Entity target) {
		if (fakeEvoker == null) {
			return;
		}

		Random rand = new Random();
		World world = fakeEvoker.world;

		for (int i = 0; i < VMConfig.VEX_NUMBER.get(); ++i) {
			BlockPos pos = new BlockPos(fakeEvoker).add(-2 + rand.nextInt(5), 1, -2 + rand.nextInt(5));

			VexEntity vex = EntityType.VEX.create(world);
			vex.moveToBlockPosAndAngles(pos, 0.0F, 0.0F);
			vex.onInitialSpawn(world, world.getDifficultyForLocation(pos), SpawnReason.MOB_SUMMONED, null, null);
			vex.setBoundOrigin(pos);

			if (VMConfig.VEX_HAS_LIMITED_LIFE.get()) {
				vex.setLimitedLife(20 * (30 + rand.nextInt(90)));
			}

			world.addEntity(vex);
		}

		fakeEvoker.playSound(SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON, 1.0F, 1.0F);
	}
}