package com.github.sejoslaw.vanillamagic.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

/**
 * Basic EntitySpell definition. This Spell is fired when Player casts Spell.
 * Works like invisible Ender Pearl which go in the Player looking direction.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class EntitySpell extends DamagingProjectileEntity {
    public EntitySpell(EntityType<? extends EntitySpell> entityType, World world) {
        super(entityType, world);
    }

    public EntitySpell setCastingEntity(LivingEntity entity) {
        this.shootingEntity = entity;

        this.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, entity.rotationPitch);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.setMotion(Vec3d.ZERO);

        Vec3d vec = this.shootingEntity.getLookVec();

        double accelX = vec.getX();
        double accelY = vec.getY();
        double accelZ = vec.getZ();

        double distance = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);

        this.accelerationX = accelX / distance * 0.1D;
        this.accelerationY = accelY / distance * 0.1D;
        this.accelerationZ = accelZ / distance * 0.1D;

        return this;
    }

    protected IParticleData getParticle() {
        return ParticleTypes.END_ROD;
    }

    protected void onImpact(RayTraceResult result) {
        this.onHit(result);
        this.remove();
    }

    protected BlockPos getBlockPos(RayTraceResult result) {
        if (result.getType() == RayTraceResult.Type.BLOCK) {
            return ((BlockRayTraceResult) result).getPos();
        } else if (result.getType() == RayTraceResult.Type.ENTITY) {
            return ((EntityRayTraceResult) result).getEntity().getPosition();
        } else {
            return this.shootingEntity.getPosition();
        }
    }

    protected abstract void onHit(RayTraceResult result);
}