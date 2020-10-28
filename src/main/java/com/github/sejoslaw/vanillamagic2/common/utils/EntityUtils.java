package com.github.sejoslaw.vanillamagic2.common.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class EntityUtils {
    /**
     * @return Formatted Player's name.
     */
    public static String getPlayerNameFormatted(PlayerEntity player) {
        if (player == null) {
            return null;
        }

        return TextUtils.getFormattedText(player.getName());
    }

    /**
     * @return List with registered types equal with the one that is specified.
     */
    public static List<EntityType<?>> getEntitiesByClassification(EntityClassification classification) {
        return ForgeRegistries.ENTITIES
                .getValues()
                .stream()
                .filter(type -> type.getClassification() == classification)
                .collect(Collectors.toList());
    }

    /**
     * Teleports Entity to the specified location.
     */
    public static void teleport(Entity entity, BlockPos newPos, RegistryKey<World> key) {
        WorldUtils.forServer(entity.getEntityWorld(), serverWorld -> {
            if (entity instanceof ServerPlayerEntity) {
                entity.fallDistance = 0;
                ((ServerPlayerEntity) entity).teleport(serverWorld.getWorld().getServer().getWorld(key), newPos.getX(), newPos.getY(), newPos.getZ(), ((ServerPlayerEntity) entity).cameraYaw, entity.rotationPitch);
            }
        });
    }

    /**
     * @return Entity which the specified Entity is looking at; otherwise null.
     */
    public static Entity getLookingAt(Entity entity) {
        if (Minecraft.getInstance().objectMouseOver instanceof EntityRayTraceResult) {
            return ((EntityRayTraceResult) Minecraft.getInstance().objectMouseOver).getEntity();
        }

        return null;
    }

    /**
     * Adds lightning bolt to the IWorld.
     */
    public static void spawnLightningBolt(IWorld world, LightningBoltEntity entity) {
        entity.setEffectOnly(false);
        world.addEntity(entity);
    }

    /**
     * Common way for setting acceleration for specified Entity.
     */
    public static void setupAcceleration(DamagingProjectileEntity entity, double accelX, double accelY, double accelZ) {
        double distance = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);

        entity.accelerationX = accelX / distance * 0.1D;
        entity.accelerationY = accelY / distance * 0.1D;
        entity.accelerationZ = accelZ / distance * 0.1D;
    }
}
