package com.github.sejoslaw.vanillamagic2.common.spells.logics;

import com.github.sejoslaw.vanillamagic2.common.entities.EntitySpell;
import com.github.sejoslaw.vanillamagic2.common.utils.EntityUtils;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class PullLogic extends EntitySpellLogic {
    public void execute(EntitySpell entitySpell, World world, RayTraceResult result) {
        if (result.getType() == RayTraceResult.Type.ENTITY) {
            EntityUtils.teleport(((EntityRayTraceResult) result).getEntity(), entitySpell.shootingEntity.getPosition(), world);
        }
    }
}
