package com.github.sejoslaw.vanillamagic.magic.spell.spells;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import com.github.sejoslaw.vanillamagic.magic.spell.Spell;
import com.github.sejoslaw.vanillamagic.common.util.TeleportUtil;
import com.github.sejoslaw.vanillamagic.util.EntityUtil;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellTeleportToEnd extends Spell {
	public SpellTeleportToEnd(int spellID, String spellName, String spellUniqueName, IWand wand,
			ItemStack itemOffHand) {
		super(spellID, spellName, spellUniqueName, wand, itemOffHand);
	}

	public boolean castSpell(PlayerEntity caster, BlockPos pos, Direction face, Vector3D hitVec) {
		try {
			if (caster.dimension == 0) {
				if (TeleportUtil.entityChangeDimension(caster, 1) != null) {
					return true;
				}
			} else if (caster.dimension == 1) {
				World world = caster.world;
				List<Entity> entities = world.loadedEntityList;

				for (int i = 0; i < entities.size(); ++i) {
					if (entities.get(i) instanceof EntityDragon) {
						EntityUtil.addChatComponentMessageNoSpam(caster, "You need to kill Dragon !!!");
						return false;
					}
				}

				if (caster instanceof ServerPlayerEntity) {
					TeleportUtil.changePlayerDimensionWithoutPortal((ServerPlayerEntity) caster, 0);
				}

				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}
}