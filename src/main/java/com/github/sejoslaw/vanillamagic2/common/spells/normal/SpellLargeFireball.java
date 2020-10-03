package com.github.sejoslaw.vanillamagic2.common.spells.normal;

import com.github.sejoslaw.vanillamagic2.common.spells.Spell;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellLargeFireball extends Spell {
    public void cast(PlayerEntity player, IWorld world, BlockPos pos, Direction face) {
        this.shootEntitySpell(world, player, (accelX, accelY, accelZ) ->
                new FireballEntity(WorldUtils.asWorld(world), player.getPosX() + accelX, player.getPosY() + 1.5D + accelY, player.getPosZ() + accelZ, accelX, accelY, accelZ));
    }
}
