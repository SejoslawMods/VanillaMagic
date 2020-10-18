package com.github.sejoslaw.vanillamagic2.common.spells.summon.logics;

import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.IWorld;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SlimeSummonLogic extends SummonEntityLogic {
    public SlimeSummonLogic() {
        super(EntityType.SLIME);
    }

    public Entity getEntity(IWorld world) {
        final String key = "Size";

        SlimeEntity slime = EntityType.SLIME.create(WorldUtils.asWorld(world));
        CompoundNBT nbt = new CompoundNBT();

        slime.writeAdditional(nbt);
        nbt.remove(key);
        nbt.putInt(key, 4);
        slime.readAdditional(nbt);

        return slime;
    }
}
