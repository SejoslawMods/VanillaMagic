package com.github.sejoslaw.vanillamagic2.common.spells.logics;

import com.github.sejoslaw.vanillamagic2.common.entities.EntitySpell;
import com.github.sejoslaw.vanillamagic2.common.utils.EntityUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IWorld;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class PullLogic extends EntitySpellLogic {
    public void execute(EntitySpell entitySpell, IWorld world, RayTraceResult result) {
        if (result.getType() == RayTraceResult.Type.ENTITY) {
            EntityUtils.teleport(((EntityRayTraceResult) result).getEntity(), entitySpell.func_234616_v_().getPosition(), WorldUtils.asWorld(world).getDimensionKey());
        }
    }
}
