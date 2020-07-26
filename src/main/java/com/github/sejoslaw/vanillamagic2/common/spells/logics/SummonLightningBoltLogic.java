package com.github.sejoslaw.vanillamagic2.common.spells.logics;

import com.github.sejoslaw.vanillamagic2.common.entities.EntitySpell;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SummonLightningBoltLogic extends EntitySpellLogic {
    public void execute(EntitySpell entitySpell, World world, RayTraceResult result) {
        if (result.getType() == RayTraceResult.Type.BLOCK) {
            BlockPos pos = ((BlockRayTraceResult) result).getPos();
            world.addEntity(new LightningBoltEntity(world, pos.getX(), pos.getY(), pos.getZ(), false));
        }
    }
}
