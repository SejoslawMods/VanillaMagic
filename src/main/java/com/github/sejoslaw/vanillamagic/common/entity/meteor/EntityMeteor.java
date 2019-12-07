package com.github.sejoslaw.vanillamagic.common.entity.meteor;

import com.github.sejoslaw.vanillamagic.common.config.VMConfig;
import com.github.sejoslaw.vanillamagic.common.explosions.VMExplosion;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

/**
 * Class which represents Meteor itself.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EntityMeteor extends FireballEntity {
    public EntityMeteor(EntityType<? extends FireballEntity> entityType, World world) {
        super(entityType, world);
    }

    public void setupMeteor(double spawnMeteorX, double spawnMeteorY, double spawnMeteorZ, double accelX, double accelY, double accelZ) {
        setLocationAndAngles(spawnMeteorX, spawnMeteorY, spawnMeteorZ, this.rotationYaw, this.rotationPitch);
        setPosition(spawnMeteorX, spawnMeteorY, spawnMeteorZ);

        double accel = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
        this.setMotion(0, accelY / accel * 0.1D, 0);
    }

    protected void onImpact(RayTraceResult result) {
        super.onImpact(result);

        if (this.world.isRemote) {
            return;
        }

        VMExplosion explosion = new VMExplosion(this.world, this.shootingEntity, this.getPosition(), VMConfig.BASIC_METEOR_EXPLOSION_POWER, true, Explosion.Mode.DESTROY);
        explosion.doExplosion();
    }
}