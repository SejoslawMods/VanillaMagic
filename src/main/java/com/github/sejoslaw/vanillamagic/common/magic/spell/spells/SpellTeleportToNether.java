package com.github.sejoslaw.vanillamagic.magic.spell.spells;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import com.github.sejoslaw.vanillamagic.magic.spell.Spell;
import com.github.sejoslaw.vanillamagic.common.util.TeleportUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellTeleportToNether extends Spell {
	public SpellTeleportToNether(int spellID, String spellName, String spellUniqueName, IWand wand,
			ItemStack itemOffHand) {
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public boolean castSpell(PlayerEntity caster, BlockPos pos, Direction face, Vector3D hitVec) {
		try {
			if (caster instanceof ServerPlayerEntity) {
				if (caster.dimension == 0) {
					TeleportUtil.changePlayerDimensionWithoutPortal((ServerPlayerEntity) caster, -1);
					return true;
				} else if (caster.dimension == -1) {
					TeleportUtil.changePlayerDimensionWithoutPortal((ServerPlayerEntity) caster, 0);
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
}