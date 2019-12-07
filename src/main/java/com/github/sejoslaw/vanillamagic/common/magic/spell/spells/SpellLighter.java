package com.github.sejoslaw.vanillamagic.common.magic.spell.spells;

import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import com.github.sejoslaw.vanillamagic.common.magic.spell.Spell;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellLighter extends Spell {
	public SpellLighter(int spellID, String spellName, String spellUniqueName, IWand wand, ItemStack itemOffHand) {
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public boolean castSpell(PlayerEntity caster, BlockPos pos, Direction face, Vector3D hitVec) {
		// RightClickBlock
		if (pos == null) {
			return false;
		}

		World world = caster.world;
		pos = pos.offset(face);

		if (!world.isAirBlock(pos)) {
			return false;
		}

		world.playSound(caster, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, new Random().nextFloat() * 0.4F + 0.8F);
		world.setBlockState(pos, Blocks.FIRE.getDefaultState(), 11);

		return true;
	}
}