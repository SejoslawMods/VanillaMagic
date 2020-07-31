package com.github.sejoslaw.vanillamagic2.common.spells.evokers;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EvokerSpellFangs extends EvokerSpell {
    public void cast(World world, PlayerEntity player, Entity target) {
        double minDifY = Math.min(target.getPosY(), player.getPosY());
        double maxDifY = Math.max(target.getPosY(), player.getPosY()) + 1.0D;
        float distance = (float) MathHelper.atan2(target.getPosZ() - player.getPosZ(), target.getPosX() - player.getPosX());

        if (player.getDistanceSq(target) < 9.0D) {
            for (int i = 0; i < 5; ++i) {
                float distance1 = distance + (float) i * (float) Math.PI * 0.4F;
                spawnFangs(world, player, player.getPosX() + (double) MathHelper.cos(distance1) * 1.5D, player.getPosZ() + (double) MathHelper.sin(distance1) * 1.5D, minDifY, maxDifY, distance1, 0);
            }

            for (int i = 0; i < 8; ++i) {
                float distance1 = distance + (float) i * (float) Math.PI * 2.0F / 8.0F + ((float) Math.PI * 2F / 5F);
                spawnFangs(world, player, player.getPosX() + (double) MathHelper.cos(distance1) * 2.5D, player.getPosZ() + (double) MathHelper.sin(distance1) * 2.5D, minDifY, maxDifY, distance1, 3);
            }
        } else {
            for (int l = 0; l < 16; ++l) {
                double distance1 = 1.25D * (double) (l + 1);
                spawnFangs(world, player, player.getPosX() + (double) MathHelper.cos(distance) * distance1, player.getPosZ() + (double) MathHelper.sin(distance) * distance1, minDifY, maxDifY, distance, l);
            }
        }

        player.playSound(SoundEvents.ENTITY_EVOKER_PREPARE_ATTACK, 1.0F, 1.0F);
    }

    private void spawnFangs(World world, PlayerEntity player, double nextX, double nextZ, double minDifY, double maxDifY, float distance, int warmupDelayTicks) {
        BlockPos pos = new BlockPos(nextX, maxDifY, nextZ);
        boolean flag = false;
        double maxY = 0.0D;

        while (true) {
            BlockPos downPos = pos.down();
            BlockState stateDown = world.getBlockState(downPos);

            if (stateDown.isSolidSide(world, downPos, Direction.UP)) {
                if (!world.isAirBlock(pos)) {
                    BlockState state = world.getBlockState(pos);
                    VoxelShape shape = state.getCollisionShape(world, pos);

                    if (!shape.isEmpty()) {
                        maxY = shape.getEnd(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            }

            pos = pos.down();

            if (pos.getY() < MathHelper.floor(minDifY) - 1) {
                break;
            }
        }

        if (flag) {
            world.addEntity(new EvokerFangsEntity(world, nextX, (double) pos.getY() + maxY, nextZ, distance, warmupDelayTicks, player));
        }
    }
}
