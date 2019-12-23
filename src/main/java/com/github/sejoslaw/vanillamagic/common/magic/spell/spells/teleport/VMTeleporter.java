package com.github.sejoslaw.vanillamagic.common.magic.spell.spells.teleport;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.Teleporter;
import net.minecraft.world.server.ServerWorld;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VMTeleporter extends Teleporter {
    public double posX, posY, posZ;

    public VMTeleporter(ServerWorld worldServer, double posX, double posY, double posZ) {
        super(worldServer);

        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    public boolean func_222268_a(Entity entity, float rotationYaw) { // placeInExistingPortal
        placeInPortal(entity, rotationYaw);
        return true;
    }

    public void placeInPortal(Entity entity, float rotationYaw) {
        if (entity instanceof ServerPlayerEntity) {
            ((ServerPlayerEntity) entity).connection.setPlayerLocation(posX, posY, posZ, entity.rotationYaw, entity.rotationPitch);
            ((ServerPlayerEntity) entity).connection.captureCurrentPosition();
        } else {
            entity.setLocationAndAngles(posX, posY, posZ, entity.rotationYaw, entity.rotationPitch);
        }
    }
}