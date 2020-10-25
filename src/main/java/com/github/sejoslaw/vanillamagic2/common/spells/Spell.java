package com.github.sejoslaw.vanillamagic2.common.spells;

import com.github.sejoslaw.vanillamagic2.common.functions.Function3;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class Spell {
    /**
     * Casts current spell.
     */
    public abstract void cast(PlayerEntity player, World world, BlockPos pos, Direction face);

    /**
     * Shoots specified Projectile Entity.
     */
    protected void shootEntitySpell(World world, PlayerEntity player, Function3<Double, Double, Double, DamagingProjectileEntity> projectileFactory) {
        Vec3d lookingAt = player.getLookVec();

        double accelX = lookingAt.getX();
        double accelY = lookingAt.getY();
        double accelZ = lookingAt.getZ();

        DamagingProjectileEntity projectileEntity = projectileFactory.apply(accelX, accelY, accelZ);

        projectileEntity.shootingEntity = player;

        projectileEntity.setLocationAndAngles(
                player.getPosX() + accelX,
                player.getPosY() + accelY + 1.5D,
                player.getPosZ() + accelZ,
                player.rotationYaw,
                player.rotationPitch);

        projectileEntity.setMotion(Vec3d.ZERO);

        double distance = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);

        projectileEntity.accelerationX = accelX / distance * 0.1D;
        projectileEntity.accelerationY = accelY / distance * 0.1D;
        projectileEntity.accelerationZ = accelZ / distance * 0.1D;

        world.addEntity(projectileEntity);
    }
}
