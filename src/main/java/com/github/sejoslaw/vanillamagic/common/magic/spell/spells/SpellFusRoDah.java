package com.github.sejoslaw.vanillamagic.common.magic.spell.spells;

import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import com.github.sejoslaw.vanillamagic.common.magic.spell.Spell;
import com.github.sejoslaw.vanillamagic.common.util.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellFusRoDah extends Spell {
	public SpellFusRoDah(int spellID, String spellName, String spellUniqueName, IWand wand, ItemStack itemOffHand) {
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public boolean castSpell(PlayerEntity caster, BlockPos pos, Direction face, Vector3D hitVec) {
		if (pos != null) {
			return false;
		}

		// it will be (2 *SIZE)x(2 *SIZE)x(2 * SIZE) area of effect
		int SIZE = 8;
		float strenght = 2.0f;
		double casterX = caster.posX;
		double casterY = caster.posY;
		double casterZ = caster.posZ;

		BlockPos casterPos = new BlockPos(casterX, casterY, casterZ);
		AxisAlignedBB aabb = new AxisAlignedBB(casterPos);
		aabb = aabb.expand(SIZE, SIZE, SIZE);

		World world = caster.world;
		List<Entity> entitiesInAABB = world.getEntitiesWithinAABBExcludingEntity(caster, aabb);

		for (Entity entity : entitiesInAABB) {
			EntityUtil.knockBack(caster, entity, strenght);
		}

		return true;
	}
}