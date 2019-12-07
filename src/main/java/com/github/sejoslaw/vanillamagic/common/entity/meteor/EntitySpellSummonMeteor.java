package com.github.sejoslaw.vanillamagic.common.entity.meteor;

import com.github.sejoslaw.vanillamagic.common.entity.EntitySpell;
import com.github.sejoslaw.vanillamagic.core.VMEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Class which represents EntitySpell which will spawn Meteor. Player shot this
 * Entity and after hitting ground This will spawn EntityMeteor.
 *
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EntitySpellSummonMeteor extends EntitySpell {
    public EntitySpellSummonMeteor(EntityType<? extends EntitySpell> entityType, World world) {
        super(entityType, world);
    }

    protected void onHit(RayTraceResult result) {
        BlockPos hitPos = this.getBlockPos(result);

        if (hitPos == this.shootingEntity.getPosition()) {
            return;
        }

        EntityMeteor meteor = new EntityMeteor(VMEntities.METEOR_ENTITY_TYPE, this.world);
        meteor.setupMeteor(hitPos.getX(), 300, hitPos.getZ(), 0, this.accelerationY, 0);

        this.world.addEntity(meteor);
    }
}