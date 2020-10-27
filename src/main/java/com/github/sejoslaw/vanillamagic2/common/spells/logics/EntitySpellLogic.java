package com.github.sejoslaw.vanillamagic2.common.spells.logics;

import com.github.sejoslaw.vanillamagic2.common.entities.EntitySpell;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IWorld;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public abstract class EntitySpellLogic {
    protected BlockPos getBlockPos(EntitySpell entitySpell, RayTraceResult result) {
        if (result.getType() == RayTraceResult.Type.BLOCK) {
            return ((BlockRayTraceResult) result).getPos();
        } else if (result.getType() == RayTraceResult.Type.ENTITY) {
            return ((EntityRayTraceResult) result).getEntity().getPosition();
        } else {
            return entitySpell.func_234616_v_().getPosition();
        }
    }

    public abstract void execute(EntitySpell entitySpell, IWorld world, RayTraceResult result);
}
