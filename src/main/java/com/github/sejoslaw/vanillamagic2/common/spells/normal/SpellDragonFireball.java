package com.github.sejoslaw.vanillamagic2.common.spells.normal;

import com.github.sejoslaw.vanillamagic2.common.spells.Spell;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.DragonFireballEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellDragonFireball extends Spell {
    public void cast(PlayerEntity player, IWorld world, BlockPos pos, Direction face) {
        this.shootEntitySpell(world, player, (accelX, accelY, accelZ) -> new DragonFireballEntity(WorldUtils.asWorld(world), player, accelX, accelY, accelZ));
    }
}
