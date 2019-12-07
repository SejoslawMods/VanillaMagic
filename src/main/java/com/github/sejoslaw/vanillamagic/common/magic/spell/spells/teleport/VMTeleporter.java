package com.github.sejoslaw.vanillamagic.magic.spell.spells.teleport;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Teleporter;
import net.minecraft.world.ServerWorld;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class VMTeleporter extends Teleporter {
	public final ServerWorld worldServerInstance;
	public double posX, posY, posZ;

	public VMTeleporter(ServerWorld worldServer, double posX, double posY, double posZ) {
		super(worldServer);

		this.worldServerInstance = worldServer;
		this.posX = posX;
		this.posY = posY;
		this.posZ = posZ;
	}

	public void placeInPortal(Entity entity, float rotationYaw) {
		BlockPos entityPos = new BlockPos(posX, posY, posZ);
		this.worldServerInstance.getBlockState(entityPos).getBlock();
		entity.setPosition(this.posX, this.posY, this.posZ);
		entity.motionX = 0.0f;
		entity.motionY = 0.0f;
		entity.motionZ = 0.0f;
	}

	public boolean placeInExistingPortal(Entity entity, float rotationYaw) {
		placeInPortal(entity, rotationYaw);
		return true;
	}
}