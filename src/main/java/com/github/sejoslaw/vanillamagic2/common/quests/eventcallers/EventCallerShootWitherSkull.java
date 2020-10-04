package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestShootWitherSkull;
import com.github.sejoslaw.vanillamagic2.common.utils.EntityUtils;
import com.github.sejoslaw.vanillamagic2.common.utils.WorldUtils;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EventCallerShootWitherSkull extends EventCaller<QuestShootWitherSkull> {
    @SubscribeEvent
    public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        this.executor.onPlayerInteract(event, (player, world, pos, face) ->
                this.executor.withHands(player, (leftHandStack, rightHandStack) -> {
                    world.playEvent(player, 1024, new BlockPos(player.getPosition()), 0);

                    Vector3d lookingAt = player.getLookVec();
                    double accelX = lookingAt.getX();
                    double accelY = lookingAt.getY();
                    double accelZ = lookingAt.getZ();

                    WitherSkullEntity entityWitherSkull = new WitherSkullEntity(
                            WorldUtils.asWorld(world),
                            player.getPosX() + accelX,
                            player.getPosY() + 1.5D + accelY,
                            player.getPosZ() + accelZ,
                            accelX,
                            accelY,
                            accelZ);

                    entityWitherSkull.setShooter(player);
                    entityWitherSkull.setMotion(0, 0, 0);
                    EntityUtils.setupAcceleration(entityWitherSkull, accelX, accelY, accelZ);

                    world.addEntity(entityWitherSkull);
                }));
    }
}
