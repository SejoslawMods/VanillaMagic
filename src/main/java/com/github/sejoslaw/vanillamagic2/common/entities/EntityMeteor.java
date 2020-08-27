package com.github.sejoslaw.vanillamagic2.common.entities;

import com.github.sejoslaw.vanillamagic2.common.explosions.VMExplosion;
import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityMeteor extends FireballEntity {
    public EntityMeteor(EntityType<? extends EntityMeteor> entityType, World world) {
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

        VMExplosion explosion = new VMExplosion(this.world, this.shootingEntity, this.getPosition(), VMForgeConfig.METEOR_EXPLOSION_POWER.get(), true, Explosion.Mode.DESTROY);
        explosion.doExplosion();
    }
}
