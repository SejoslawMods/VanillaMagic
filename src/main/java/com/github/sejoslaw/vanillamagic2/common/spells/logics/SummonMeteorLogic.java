package com.github.sejoslaw.vanillamagic2.common.spells.logics;

import com.github.sejoslaw.vanillamagic2.common.entities.EntityMeteor;
import com.github.sejoslaw.vanillamagic2.common.entities.EntitySpell;
import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.registries.EntityRegistry;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IWorld;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SummonMeteorLogic extends EntitySpellLogic {
    public void execute(EntitySpell entitySpell, IWorld world, RayTraceResult result) {
        BlockPos hitPos = this.getBlockPos(entitySpell, result);

        if (hitPos == entitySpell.player.getPosition()) {
            return;
        }

        EntityMeteor meteor = new EntityMeteor(EntityRegistry.METEOR.get(), WorldUtils.asWorld(world));
        meteor.setupMeteor(hitPos.getX(), 300, hitPos.getZ(), 0, -VMForgeConfig.METEOR_ACCELERATION.get(), 0);

        world.addEntity(meteor);
    }
}
