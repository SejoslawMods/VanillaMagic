package com.github.sejoslaw.vanillamagic.common.util;

import com.github.sejoslaw.vanillamagic.api.event.EventTeleportEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public final class TeleportUtil {
    private TeleportUtil() {
    }

    public static Entity entityChangeDimension(Entity entity, DimensionType newDim) {
        if (!EventUtil.postEvent(new EventTeleportEntity.ChangeDimension(entity, entity.getPosition(), newDim))) {
            return entity.changeDimension(newDim);
        }

        return null;
    }

    public static void teleportEntity(Entity entity, BlockPos newPos, World world) {
        if (entity instanceof ServerPlayerEntity && world instanceof ServerWorld) {
            ((ServerPlayerEntity) entity).teleport((ServerWorld) world, newPos.getX(), newPos.getY(), newPos.getZ(), ((ServerPlayerEntity) entity).cameraYaw, entity.rotationPitch);
        } else {
            entity.teleportKeepLoaded(newPos.getX(), newPos.getY(), newPos.getZ());
        }
    }
}