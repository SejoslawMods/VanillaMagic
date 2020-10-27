package com.github.sejoslaw.vanillamagic2.common.spells.normal;

import com.github.sejoslaw.vanillamagic2.common.spells.Spell;
import com.github.sejoslaw.vanillamagic2.common.utils.EntityUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.TextUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellTeleportToEnd extends Spell {
    public void cast(PlayerEntity player, IWorld world, BlockPos pos, Direction face) {
        if (WorldUtils.getIsRemote(world)) {
            return;
        }

        if (player.getEntityWorld().getDimensionKey().getLocation().toString().equals(World.OVERWORLD.getLocation().toString())) {
            ServerWorld serverWorld = WorldUtils.asWorld(world).getServer().getWorld(World.THE_END);
            player.changeDimension(serverWorld);
        } else {
            List<EnderDragonEntity> entities = WorldUtils.getEntities(world, EnderDragonEntity.class, player.getPosition(), 256, entity -> true);

            if (entities.size() > 0) {
                TextUtils.addChatMessage("vm.message.killDragon");
            } else {
                EntityUtils.teleport(player, player.getPosition(), World.OVERWORLD);
            }
        }
    }
}
