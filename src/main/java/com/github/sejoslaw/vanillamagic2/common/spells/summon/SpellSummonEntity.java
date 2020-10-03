package com.github.sejoslaw.vanillamagic2.common.spells.summon;

import com.github.sejoslaw.vanillamagic2.common.files.VMForgeConfig;
import com.github.sejoslaw.vanillamagic2.common.registries.SummonEntityLogicRegistry;
import com.github.sejoslaw.vanillamagic2.common.spells.Spell;
import net.minecraft.entity.AgeableEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

/**
 * @author Sejoslaw - https://github.com/Sejoslaw
 */
public class SpellSummonEntity extends Spell {
    private final EntityType<? extends Entity> entityType;

    public SpellSummonEntity(EntityType<? extends Entity> entityType) {
        this.entityType = entityType;
    }

    public void cast(PlayerEntity player, IWorld world, BlockPos pos, Direction face) {
        Entity entity = SummonEntityLogicRegistry.getEntity(world, entityType);

        if (entity == null) {
            return;
        }

        if (entity instanceof AgeableEntity) {
            ((AgeableEntity) entity).setGrowingAge(1);
        }

        BlockPos spawnPos = pos.offset(face);
        entity.setLocationAndAngles(spawnPos.getX() + 0.5D, spawnPos.getY(), spawnPos.getZ() + 0.5D, player.rotationYaw, 0.0F);

        world.addEntity(entity);

        Entity horse = SummonEntityLogicRegistry.getHorse(world, entityType);

        if ((horse != null) && (SummonEntityLogicRegistry.getPercent(entityType) < VMForgeConfig.PERCENT_FOR_SPAWN_HOSTILE_ON_HORSE.get())) {
            entity.startRiding(horse);
        }
    }
}
