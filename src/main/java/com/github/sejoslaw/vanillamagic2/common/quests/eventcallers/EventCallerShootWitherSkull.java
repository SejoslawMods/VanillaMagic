package com.github.sejoslaw.vanillamagic2.common.quests.eventcallers;

import com.github.sejoslaw.vanillamagic2.common.quests.EventCaller;
import com.github.sejoslaw.vanillamagic2.common.quests.types.QuestShootWitherSkull;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
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
                    world.playEvent(player, 1024, new BlockPos(player), 0);

                    Vec3d lookingAt = player.getLookVec();
                    double accelX = lookingAt.getX();
                    double accelY = lookingAt.getY();
                    double accelZ = lookingAt.getZ();

                    WitherSkullEntity entityWitherSkull = new WitherSkullEntity(
                            world,
                            player.getPosX() + accelX,
                            player.getPosY() + 1.5D + accelY,
                            player.getPosZ() + accelZ,
                            accelX,
                            accelY,
                            accelZ);

                    entityWitherSkull.shootingEntity = player;
                    entityWitherSkull.setMotion(0, 0, 0);

                    double accelDelta = MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);

                    entityWitherSkull.accelerationX = accelX / accelDelta * 0.1D;
                    entityWitherSkull.accelerationY = accelY / accelDelta * 0.1D;
                    entityWitherSkull.accelerationZ = accelZ / accelDelta * 0.1D;

                    world.addEntity(entityWitherSkull);
                }));
    }
}
