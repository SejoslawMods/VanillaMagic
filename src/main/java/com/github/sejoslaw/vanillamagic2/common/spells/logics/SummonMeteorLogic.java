package com.github.sejoslaw.vanillamagic2.common.spells.logics;

import com.github.sejoslaw.vanillamagic2.common.entities.EntityMeteor;
import com.github.sejoslaw.vanillamagic2.common.entities.EntitySpell;
import com.github.sejoslaw.vanillamagic2.common.registries.VMNetworkRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IWorld;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SummonMeteorLogic extends EntitySpellLogic {
    public void execute(EntitySpell entitySpell, IWorld world, RayTraceResult result) {
        BlockPos hitPos = this.getBlockPos(entitySpell, result);
        Entity shooter = entitySpell.func_234616_v_();

        if (shooter == null || hitPos == shooter.getPosition()) {
            return;
        }

        EntityMeteor meteor = EntityMeteor.create(world, hitPos.getX(), hitPos.getZ(), shooter);

        world.addEntity(meteor);
    }
}
