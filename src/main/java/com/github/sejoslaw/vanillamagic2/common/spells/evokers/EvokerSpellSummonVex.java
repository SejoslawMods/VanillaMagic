package com.github.sejoslaw.vanillamagic2.common.spells.evokers;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.monster.VexEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class EvokerSpellSummonVex extends EvokerSpell {
    public void cast(World world, PlayerEntity player, Entity target) {
        for (int i = 0; i < VMForgeConfig.VEX_NUMBER.get(); ++i) {
            BlockPos pos = new BlockPos(player).add(-2 + rand.nextInt(5), 1, -2 + rand.nextInt(5));

            VexEntity vex = EntityType.VEX.create(world);
            vex.moveToBlockPosAndAngles(pos, 0.0F, 0.0F);
            vex.onInitialSpawn(world, world.getDifficultyForLocation(pos), SpawnReason.MOB_SUMMONED, null, null);
            vex.setBoundOrigin(pos);

            if (VMForgeConfig.VEX_HAS_LIMITED_LIFE.get()) {
                vex.setLimitedLife(20 * (30 + rand.nextInt(90)));
            }

            world.addEntity(vex);
        }

        player.playSound(SoundEvents.ENTITY_EVOKER_PREPARE_SUMMON, 1.0F, 1.0F);
    }
}
