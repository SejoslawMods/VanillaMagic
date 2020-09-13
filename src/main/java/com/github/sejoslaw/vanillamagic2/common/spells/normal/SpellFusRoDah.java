package com.github.sejoslaw.vanillamagic2.common.spells.normal;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.spells.Spell;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellFusRoDah extends Spell {
    public void cast(PlayerEntity player, World world, BlockPos pos, Direction face) {
        if (world.isRemote) {
            return;
        }

        int distance = VMForgeConfig.SPELL_FUS_RO_DAH_DISTANCE.get();

        double casterX = player.getPosX();
        double casterY = player.getPosY();
        double casterZ = player.getPosZ();

        BlockPos casterPos = new BlockPos(casterX, casterY, casterZ);
        AxisAlignedBB aabb = new AxisAlignedBB(casterPos);
        aabb = aabb.expand(distance, distance, distance);
        aabb = aabb.expand(-distance, -distance, -distance);

        List<Entity> entitiesInAABB = world.getEntitiesWithinAABBExcludingEntity(player, aabb);

        for (Entity entity : entitiesInAABB) {
            this.knockBack(player, entity, VMForgeConfig.SPELL_FUS_RO_DAH_STRENGTH.get());
        }
    }

    private void knockBack(PlayerEntity player, Entity entity, double strength) {
        double xRatio = player.getPosX() - entity.getPosX();
        double zRatio = player.getPosZ() - entity.getPosZ();

        entity.isAirBorne = true;
        float f = MathHelper.sqrt(xRatio * xRatio + zRatio * zRatio);

        double motionX = entity.getMotion().getX();
        double motionY = entity.getMotion().getY();
        double motionZ = entity.getMotion().getZ();

        motionX /= 2.0D;
        motionZ /= 2.0D;

        motionX -= xRatio / (double) f * strength;
        motionZ -= zRatio / (double) f * strength;

        if (entity.onGround) {
            motionY /= 2.0D;
            motionY += strength;

            if (motionY > 0.4000000059604645D) {
                motionY = 0.4000000059604645D;
            }
        }

        entity.setMotion(motionX, motionY, motionZ);
    }
}
