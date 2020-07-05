package com.github.sejoslaw.vanillamagic.common.item.evokercrystal.spell;

//TODO: Fix long range Fangs attaks.

import com.github.sejoslaw.vanillamagic.api.magic.IEvokerSpell;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EvokerSpellFangAttack implements IEvokerSpell {
	public int getSpellID() {
		return 2;
	}

	public String getSpellName() {
		return "Fangs";
	}

	public void castSpell(Entity caster, Entity target) {
		LivingEntity fakeEvoker = (LivingEntity) caster;

		if ((fakeEvoker == null) || (target == null)) {
			return;
		}

		double minDifY = Math.min(target.getPosY(), fakeEvoker.getPosY());
		double maxDifY = Math.max(target.getPosY(), fakeEvoker.getPosY()) + 1.0D;
		float distance = (float) MathHelper.atan2(target.getPosZ() - fakeEvoker.getPosZ(), target.getPosX() - fakeEvoker.getPosX());

		if (fakeEvoker.getDistanceSq(target) < 9.0D) {
			for (int i = 0; i < 5; ++i) {
				float distance1 = distance + (float) i * (float) Math.PI * 0.4F;
				spawnFangs(fakeEvoker, fakeEvoker.getPosX() + (double) MathHelper.cos(distance1) * 1.5D, fakeEvoker.getPosZ() + (double) MathHelper.sin(distance1) * 1.5D, minDifY, maxDifY, distance1, 0);
			}

			for (int k = 0; k < 8; ++k) {
				float distance1 = distance + (float) k * (float) Math.PI * 2.0F / 8.0F + ((float) Math.PI * 2F / 5F);
				spawnFangs(fakeEvoker, fakeEvoker.getPosX() + (double) MathHelper.cos(distance1) * 2.5D, fakeEvoker.getPosZ() + (double) MathHelper.sin(distance1) * 2.5D, minDifY, maxDifY, distance1, 3);
			}

			caster.playSound(SoundEvents.ENTITY_EVOKER_PREPARE_ATTACK, 1.0F, 1.0F);
		} else {
			for (int l = 0; l < 16; ++l) {
				double distance1 = 1.25D * (double) (l + 1);
				spawnFangs(fakeEvoker, fakeEvoker.getPosX() + (double) MathHelper.cos(distance) * distance1, fakeEvoker.getPosZ() + (double) MathHelper.sin(distance) * distance1, minDifY, maxDifY, distance, l);
			}

			caster.playSound(SoundEvents.ENTITY_EVOKER_PREPARE_ATTACK, 1.0F, 1.0F);
		}
	}

	private void spawnFangs(LivingEntity fakeEvoker, double nextX, double nextZ, double minDifY, double maxDifY, float distance, int warmupDelayTicks) {
		BlockPos pos = new BlockPos(nextX, maxDifY, nextZ);
		boolean flag = false;
		double maxY = 0.0D;
		World world = fakeEvoker.world;

		while (true) {
			BlockPos downPos = pos.down();
			BlockState stateDown = world.getBlockState(downPos);

			if (stateDown.isSolidSide(world, downPos, Direction.UP)) {
				if (!world.isAirBlock(pos)) {
					BlockState state = world.getBlockState(pos);
					VoxelShape shape = state.getCollisionShape(world, pos);

					if (!shape.isEmpty()) {
						maxY = shape.getEnd(Direction.Axis.Y);
					}
				}

				flag = true;
				break;
			}

			pos = pos.down();

			if (pos.getY() < MathHelper.floor(minDifY) - 1) {
				break;
			}
		}

		if (flag) {
			world.addEntity(new EvokerFangsEntity(world, nextX, (double) pos.getY() + maxY, nextZ, distance, warmupDelayTicks, fakeEvoker));
		}
	}
}
