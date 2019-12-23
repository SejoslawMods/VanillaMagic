package com.github.sejoslaw.vanillamagic.common.magic.spell.spells;

import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import com.github.sejoslaw.vanillamagic.common.magic.spell.Spell;
import com.github.sejoslaw.vanillamagic.common.util.TeleportUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.DimensionType;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellTeleportToNether extends Spell {
    public SpellTeleportToNether(int spellID, String spellName, String spellUniqueName, IWand wand, ItemStack itemOffHand) {
        super(spellID, spellName, spellUniqueName, wand, itemOffHand);
    }

    public boolean castSpell(PlayerEntity caster, BlockPos pos, Direction face, Vec3d hitVec) {
        try {
            if (caster.dimension == DimensionType.OVERWORLD) {
                TeleportUtil.entityChangeDimension(caster, DimensionType.THE_NETHER);
            } else {
                TeleportUtil.entityChangeDimension(caster, DimensionType.OVERWORLD);
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}