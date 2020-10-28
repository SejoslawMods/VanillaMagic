package com.github.sejoslaw.vanillamagic2.common.spells.logics;

import com.github.sejoslaw.vanillamagic2.common.entities.EntitySpell;
import com.github.sejoslaw.vanillamagic2.common.utils.EntityUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IWorld;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class TeleportLogic extends EntitySpellLogic {
    public void execute(EntitySpell entitySpell, IWorld world, RayTraceResult result) {
        EntityUtils.teleport(entitySpell.func_234616_v_(), this.getBlockPos(entitySpell, result), WorldUtils.getId(world));
    }
}
