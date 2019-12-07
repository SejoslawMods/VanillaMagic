package com.github.sejoslaw.vanillamagic.common.entity;

import com.github.sejoslaw.vanillamagic.common.util.TeleportUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Class which defines Pull Entity Spell.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EntitySpellPull extends EntitySpell {
    public EntitySpellPull(EntityType<? extends EntitySpell> entityType, World world) {
        super(entityType, world);
    }

    protected void onHit(RayTraceResult result) {
        if (result.getType() == RayTraceResult.Type.ENTITY) {
            TeleportUtil.teleportEntity(((EntityRayTraceResult) result).getEntity(), this.shootingEntity.getPosition(), this.shootingEntity.world);
        }
    }
}