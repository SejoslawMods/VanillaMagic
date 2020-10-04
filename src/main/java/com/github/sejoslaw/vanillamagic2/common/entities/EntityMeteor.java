package com.github.sejoslaw.vanillamagic2.common.entities;

import com.github.sejoslaw.vanillamagic2.common.explosions.VMExplosion;
import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class EntityMeteor extends FireballEntity {
    public EntityMeteor(EntityType<? extends EntityMeteor> entityType, World world) {
        super(entityType, world);
    }

    public void setupMeteor(double spawnMeteorX, double spawnMeteorY, double spawnMeteorZ, double accelX, double accelY, double accelZ) {
        this.setLocationAndAngles(spawnMeteorX, spawnMeteorY, spawnMeteorZ, this.rotationYaw, this.rotationPitch);
        this.setPosition(spawnMeteorX, spawnMeteorY, spawnMeteorZ);
        this.setMotion(Vector3d.ZERO);

        double distance = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);

        this.accelerationX = accelX / distance * 0.1D;
        this.accelerationY = accelY / distance * 0.1D;
        this.accelerationZ = accelZ / distance * 0.1D;
    }

    public ItemStack getItem() {
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
}
