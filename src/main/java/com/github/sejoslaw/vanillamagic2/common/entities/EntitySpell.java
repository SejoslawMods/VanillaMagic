package com.github.sejoslaw.vanillamagic2.common.entities;

import com.github.sejoslaw.vanillamagic2.common.spells.logics.EntitySpellLogic;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EntitySpell extends DamagingProjectileEntity {
    private EntitySpellLogic logic;

    public EntitySpell(EntityType<? extends EntitySpell> entityType, World world) {
        super(entityType, world);
    }

    public EntitySpell withLogic(EntitySpellLogic logic) {
        this.logic = logic;
        return this;
    }

    protected IParticleData getParticle() {
        return ParticleTypes.END_ROD;
    }

    protected void onImpact(RayTraceResult result) {
        this.logic.execute(this, this.world, result);
        this.remove();
    }
}
