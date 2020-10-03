package com.github.sejoslaw.vanillamagic2.common.spells.logics;

import com.github.sejoslaw.vanillamagic2.common.entities.EntitySpell;
import com.github.sejoslaw.vanillamagic2.common.utils.EntityUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IWorld;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SummonLightningBoltLogic extends EntitySpellLogic {
    public void execute(EntitySpell entitySpell, IWorld world, RayTraceResult result) {
        if (result.getType() == RayTraceResult.Type.BLOCK) {
            BlockPos pos = ((BlockRayTraceResult) result).getPos();

            LightningBoltEntity entity = new LightningBoltEntity(EntityType.LIGHTNING_BOLT, WorldUtils.asWorld(world));
            entity.setPosition(pos.getX(), pos.getY(), pos.getZ());

            EntityUtils.spawnLightningBolt(world, entity);
            EntityUtils.spawnLightningBolt(Minecraft.getInstance().world, entity);
        }
    }
}
