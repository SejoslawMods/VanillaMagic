package com.github.sejoslaw.vanillamagic2.common.spells.normal;

import com.github.sejoslaw.vanillamagic2.common.spells.Spell;
import com.github.sejoslaw.vanillamagic2.common.utils.EntityUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellTeleportToNether extends Spell {
    public void cast(PlayerEntity player, IWorld world, BlockPos pos, Direction face) {
        if (WorldUtils.getIsRemote(world)) {
            return;
        }

        if (player.getEntityWorld().getDimensionKey().getLocation().toString().equals(World.OVERWORLD.getLocation().toString())) {
            EntityUtils.teleport(player, player.getPosition(), World.THE_NETHER);
        } else {
            EntityUtils.teleport(player, player.getPosition(), World.OVERWORLD);
        }
    }
}
