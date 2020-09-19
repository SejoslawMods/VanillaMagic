package com.github.sejoslaw.vanillamagic2.common.spells.normal;

import com.github.sejoslaw.vanillamagic2.common.spells.Spell;
import com.github.sejoslaw.vanillamagic2.common.utils.EntityUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellTeleportToNether extends Spell {
    public void cast(PlayerEntity player, World world, BlockPos pos, Direction face) {
        if (world.isRemote) {
            return;
        }

        if (player.dimension == DimensionType.OVERWORLD) {
            EntityUtils.teleport(player, player.getPosition(), DimensionType.THE_NETHER);
        } else {
            EntityUtils.teleport(player, player.getPosition(), DimensionType.OVERWORLD);
        }
    }
}
