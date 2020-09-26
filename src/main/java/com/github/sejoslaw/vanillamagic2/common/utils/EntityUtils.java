package com.github.sejoslaw.vanillamagic2.common.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class EntityUtils {
    /**
     * @return Name of the Player.
     */
    public static String getPlayerName(PlayerEntity player) {
        return player.getName().getFormattedText();
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
     * Teleports Entity to the specified location on specified DimensionType.
     */
    public static void teleport(Entity entity, BlockPos newPos, DimensionType dimensionType) {
        World destinationWorld = entity.getServer().getWorld(dimensionType);
        teleport(entity, newPos, destinationWorld);
    }

    /**
     * Teleports Entity to the specified location on specified World.
     */
    public static void teleport(Entity entity, BlockPos newPos, World world) {
        if (entity instanceof ServerPlayerEntity && world instanceof ServerWorld) {
            ((ServerPlayerEntity) entity).teleport((ServerWorld) world, newPos.getX(), newPos.getY(), newPos.getZ(), ((ServerPlayerEntity) entity).cameraYaw, entity.rotationPitch);
        } else {
            if (world != entity.world) {
                entity.changeDimension(world.getDimension().getType());
            }

            entity.teleportKeepLoaded(newPos.getX(), newPos.getY(), newPos.getZ());
        }
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
     * Adds lightning bolt to the World.
     */
    public static void spawnLightningBolt(World world, LightningBoltEntity entity) {
        if (world instanceof ClientWorld) {
            ((ClientWorld) world).addLightning(entity);
        } else {
            world.addEntity(entity);
        }
    }
}
