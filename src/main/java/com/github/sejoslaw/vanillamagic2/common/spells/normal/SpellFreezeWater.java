package com.github.sejoslaw.vanillamagic2.common.spells.normal;

import com.github.sejoslaw.vanillamagic2.common.entities.EntitySpell;
import com.github.sejoslaw.vanillamagic2.common.spells.Spell;
import com.github.sejoslaw.vanillamagic2.common.spells.logics.FreezeWaterLogic;
import com.github.sejoslaw.vanillamagic2.core.VMEntities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellFreezeWater extends Spell {
    public void cast(PlayerEntity player, World world, BlockPos pos, Direction face) {
        this.shootEntitySpell(world, player, (accelX, accelY, accelZ) -> new EntitySpell(VMEntities.SPELL, world).withLogic(new FreezeWaterLogic()));
    }
}
