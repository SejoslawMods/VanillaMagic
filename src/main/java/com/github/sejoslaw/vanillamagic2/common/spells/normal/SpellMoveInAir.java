package com.github.sejoslaw.vanillamagic2.common.spells.normal;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.spells.Spell;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellMoveInAir extends Spell {
    public void cast(PlayerEntity player, World world, BlockPos pos, Direction face) {
        Vec3d casterLookVec = player.getLookVec();

        for (double i = VMForgeConfig.SPELL_MOVE_IN_AIR_DISTANCE.get(); i > 0; i -= 1.0D) {
            double newPosX = player.getPosX() + casterLookVec.getX() * i;
            double newPosY = player.getPosY() + casterLookVec.getY() * i;
            double newPosZ = player.getPosZ() + casterLookVec.getZ() * i;

            BlockPos newPos = new BlockPos(newPosX, newPosY, newPosZ);
            BlockPos newPosHead = new BlockPos(newPosX, newPosY + 1, newPosZ);

            if ((newPosY > 0) && world.isAirBlock(newPos) && world.isAirBlock(newPosHead)) {
                player.setPositionAndUpdate(newPosX, newPosY, newPosZ);
                player.fallDistance = 0.0F;
                player.getHeldItemOffhand().grow(1);

                return;
            }
        }

        if (face == null) {
            player.getHeldItemOffhand().grow(1);
        }
    }
}
