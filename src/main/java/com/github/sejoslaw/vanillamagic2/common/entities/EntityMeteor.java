package com.github.sejoslaw.vanillamagic2.common.entities;

import com.github.sejoslaw.vanillamagic2.common.explosions.VMExplosion;
import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.registries.EntityRegistry;
import com.github.sejoslaw.vanillamagic2.common.utils.EntityUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EntityMeteor extends FireballEntity {
    public EntityMeteor(EntityType<? extends EntityMeteor> entityType, World world) {
        super(entityType, world);
    }

    public void setupMeteor(double spawnMeteorX, double spawnMeteorY, double spawnMeteorZ, double accelX, double accelY, double accelZ) {
        this.setLocationAndAngles(spawnMeteorX, spawnMeteorY, spawnMeteorZ, this.rotationYaw, this.rotationPitch);
        this.setMotion(accelX, accelY / 2, accelZ);
        EntityUtils.setupAcceleration(this, accelX, accelY, accelZ);
    }

    protected ItemStack getStack() {
        return new ItemStack(Items.FIRE_CHARGE);
    }

    protected void onImpact(RayTraceResult result) {
        super.onImpact(result);

        if (WorldUtils.getIsRemote(world)) {
            return;
        }

        VMExplosion explosion = new VMExplosion(this.world, this.func_234616_v_(), this.getPosition(), VMForgeConfig.METEOR_EXPLOSION_POWER.get(), true, Explosion.Mode.DESTROY);
        explosion.doExplosion();
    }

    public static EntityMeteor create(IWorld world, double spawnMeteorX, double spawnMeteorZ, Entity shooter) {
        EntityMeteor entity = new EntityMeteor(EntityRegistry.METEOR.get(), WorldUtils.asWorld(world));

        entity.setupMeteor(spawnMeteorX, 300, spawnMeteorZ, 0, -VMForgeConfig.METEOR_ACCELERATION.get(), 0);
        entity.setShooter(shooter);

        return entity;
    }
}
