package com.github.sejoslaw.vanillamagic.common.entity;

import com.github.sejoslaw.vanillamagic.common.util.TeleportUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Class which defines the Teleport Spell.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EntitySpellTeleport extends EntitySpell {
    public EntitySpellTeleport(EntityType<? extends EntitySpell> entityType, World world) {
        super(entityType, world);
    }

    protected void onHit(RayTraceResult result) {
        BlockPos newPos = this.getBlockPos(result);

        if (newPos == this.shootingEntity.getPosition()) {
            return;
        }

        TeleportUtil.teleportEntity(this.shootingEntity, newPos, this.shootingEntity.world);
    }
}