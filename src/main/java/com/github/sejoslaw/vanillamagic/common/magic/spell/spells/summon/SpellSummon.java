package com.github.sejoslaw.vanillamagic.common.magic.spell.spells.summon;

import com.github.sejoslaw.vanillamagic.api.event.EventSpell;
import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import com.github.sejoslaw.vanillamagic.core.VMConfig;
import com.github.sejoslaw.vanillamagic.common.magic.spell.Spell;
import com.github.sejoslaw.vanillamagic.common.util.EntityUtil;
import com.github.sejoslaw.vanillamagic.common.util.EventUtil;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Random;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class SpellSummon extends Spell {
	public SpellSummon(int spellID, String spellName, String spellUniqueName, IWand wand, ItemStack itemOffHand) {
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public boolean castSpell(PlayerEntity caster, BlockPos pos, Direction face, Vec3d hitVec) {
		if (pos == null) {
			return false;
		}

		World world = caster.world;
		BlockPos spawnPos = pos.offset(face);
		Entity entity = getEntity(world);

		if (entity == null) {
			return false;
		}

		if (entity instanceof AgeableEntity) {
			((AgeableEntity) entity).setGrowingAge(1);
		}

		entity.setLocationAndAngles(spawnPos.getX() + 0.5D, spawnPos.getY(), spawnPos.getZ() + 0.5D, caster.rotationYaw, 0.0F);

		if (this.getSpawnWithArmor()) {
			EntityUtil.addRandomArmorToEntity(entity);
		}

		if (EventUtil.postEvent(new EventSpell.Cast.SummonSpell(this, caster, world, entity))) {
			return false;
		}

		world.addEntity(entity);

		Entity horse = this.getHorse(world);

		if ((horse != null) && (this.getPercent() < VMConfig.PERCENT_FOR_SPAWN_HOSTILE_ON_HORSE.get())) {
			entity.startRiding(horse);
		}

		return true;
	}

	public boolean getSpawnWithArmor() {
		return false;
	}

	public Entity getHorse(World world) {
		return null;
	}

	public abstract Entity getEntity(World world);

	protected int getPercent() {
		return new Random().nextInt(100);
	}
}