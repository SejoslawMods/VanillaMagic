package com.github.sejoslaw.vanillamagic2.common.spells;

import com.github.sejoslaw.vanillamagic2.common.functions.Function3;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class Spell {
    /**
     * Casts current spell.
     */
    public abstract void cast(PlayerEntity player, IWorld world, BlockPos pos, Direction face);

    /**
     * Shoots specified Projectile Entity.
     */
    protected void shootEntitySpell(IWorld world, PlayerEntity player, Function3<Double, Double, Double, DamagingProjectileEntity> projectileFactory) {
        Vector3d lookingAt = player.getLookVec();

        double accelX = lookingAt.getX();
        double accelY = lookingAt.getY();
        double accelZ = lookingAt.getZ();

        DamagingProjectileEntity projectileEntity = projectileFactory.apply(accelX, accelY, accelZ);

        projectileEntity.setLocationAndAngles(player.getPosX(), player.getPosY(), player.getPosZ(), player.rotationYaw, player.rotationPitch);
        projectileEntity.setPosition(projectileEntity.getPosX(), projectileEntity.getPosY(), projectileEntity.getPosZ());
        projectileEntity.setMotion(Vector3d.ZERO);

        double distance = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);

        projectileEntity.accelerationX = accelX / distance * 0.1D;
        projectileEntity.accelerationY = accelY / distance * 0.1D;
        projectileEntity.accelerationZ = accelZ / distance * 0.1D;

        world.addEntity(projectileEntity);
    }
}
