package com.github.sejoslaw.vanillamagic.common.magic.spell.spells;

import com.github.sejoslaw.vanillamagic.api.magic.IWand;
import com.github.sejoslaw.vanillamagic.common.magic.spell.Spell;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellMoveInAir extends Spell {
    public SpellMoveInAir(int spellID, String spellName, String spellUniqueName, IWand wand, ItemStack itemOffHand) {
        super(spellID, spellName, spellUniqueName, wand, itemOffHand);
    }

    public boolean castSpell(PlayerEntity caster, BlockPos pos, Direction face, Vec3d hitVec) {
        World world = caster.world;
        double distance = 10;
        Vec3d casterLookVec = caster.getLookVec();

        // will teleport caster to the farthest blockPos between casterPos and
        // 'distance'
        for (double i = distance; i > 0; i -= 1.0D) {
            double newPosX = caster.getPosX() + casterLookVec.getX() * i;
            double newPosY = caster.getPosY() + casterLookVec.getY() * i;
            double newPosZ = caster.getPosZ() + casterLookVec.getZ() * i;

            BlockPos newPos = new BlockPos(newPosX, newPosY, newPosZ);
            BlockPos newPosHead = new BlockPos(newPosX, newPosY + 1, newPosZ);

            if ((newPosY > 0) && world.isAirBlock(newPos) && world.isAirBlock(newPosHead)) {
                caster.setPositionAndUpdate(newPosX, newPosY, newPosZ);
                caster.fallDistance = 0.0F;
                return true;
            }
        }
        return false;
    }
}