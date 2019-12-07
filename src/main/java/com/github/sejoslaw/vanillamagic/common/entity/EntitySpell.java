package com.github.sejoslaw.vanillamagic.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
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