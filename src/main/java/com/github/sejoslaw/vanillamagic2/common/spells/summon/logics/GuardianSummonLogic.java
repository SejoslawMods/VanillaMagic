package com.github.sejoslaw.vanillamagic2.common.spells.summon.logics;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.ElderGuardianEntity;
import net.minecraft.entity.monster.GuardianEntity;
import net.minecraft.world.World;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class GuardianSummonLogic extends SummonEntityLogic {
    public GuardianSummonLogic() {
        super(EntityType.GUARDIAN);
    }

    public Entity getEntity(World world) {
        return this.getPercent() < 30 ? new ElderGuardianEntity(EntityType.ELDER_GUARDIAN, world) : new GuardianEntity(EntityType.GUARDIAN, world);
    }
}
