package com.github.sejoslaw.vanillamagic2.common.spells.logics;

import com.github.sejoslaw.vanillamagic2.common.entities.EntityMeteor;
import com.github.sejoslaw.vanillamagic2.common.entities.EntitySpell;
import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.registries.EntityRegistry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SummonMeteorLogic extends EntitySpellLogic {
    public void execute(EntitySpell entitySpell, World world, RayTraceResult result) {
        BlockPos hitPos = this.getBlockPos(entitySpell, result);

        if (hitPos == entitySpell.shootingEntity.getPosition()) {
            return;
        }

        EntityMeteor meteor = new EntityMeteor(EntityRegistry.METEOR.get(), world);
        meteor.shootingEntity = entitySpell.shootingEntity;
        meteor.setupMeteor(hitPos.getX(), 300, hitPos.getZ(), 0, -VMForgeConfig.METEOR_ACCELERATION.get(), 0);

        world.addEntity(meteor);
    }
}
